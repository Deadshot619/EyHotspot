package com.ey.hotspot.ui.home.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.FragmentHomeBinding
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.MyClusterItems
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.ui.search.searchlist.SearchListFragment
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.setUpSearchBar
import com.ey.hotspot.utils.dialogs.YesNoDialog
import com.ey.hotspot.utils.extention_functions.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(), OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<MyClusterItems>,
    ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItems>,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    private var map: GoogleMap? = null

    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(Constants.LATITUDE, Constants.LONGITUDE)
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private lateinit var mClusterManager: ClusterManager<MyClusterItems>
    private var clickedVenueMarker: MyClusterItems? = null
    private var favouriteType: Boolean = false
    var mLocationRequest: LocationRequest? = null
    lateinit var mGoogleApiClient: GoogleApiClient
    var result: PendingResult<LocationSettingsResult>? = null
    val REQUEST_LOCATION = 199

    private val dialog by lazy {
        YesNoDialog(requireActivity()).apply {
            setViews(
                title = getString(R.string.login_required),
                description = getString(R.string.need_to_login),
                yes = {
                    requireActivity().goToLoginScreen()
                    this.dismiss()
                },
                no = {
                    this.dismiss()
                })
        }
    }


    override fun getLayoutId() = R.layout.fragment_home
    override fun getViewModel() = HomeFragmentViewModel::class.java
    override fun onBinding() {
        //setUpGoogleAPIClient()
        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
        //Toolbar
        activity?.setUpSearchBar(
            mBinding.toolbarLayout,
            showUpButton = false,
            enableSearchButton = false
        ) {}
        mBinding.toolbarLayout.etSearchBar.isFocusable = false

        // Prompt the user for permission.
        activity?.checkLocationPermission(view = mBinding.root, func = {
            locationPermissionGranted = true
            if (!requireActivity().isLocationEnabled()) {
                activity?.turnOnGpsDialog()
            }
            updateLocationUI()
        })


        Places.initialize(requireActivity(), getString(R.string.maps_api_key))
        placesClient = Places.createClient(requireActivity())
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val myMAPF = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        myMAPF?.getMapAsync(this)
        setUpObservers()
    }


    private fun setUpObservers() {
        mViewModel.getHotSpotResponse.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it.status) {
                    setupClusters()
                    setUpHotSpotDataInCluster(it.data)
                } else {
                    showMessage(it.message, true)
                }
            }
        })

        //Change Favourite
        mViewModel.markFavourite.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { item ->
                mClusterManager.updateItem(item.apply { changeFavourite(!mIsFavourite) })
            }
        })
    }

    private fun setUpGoogleAPIClient() {
        mGoogleApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        mGoogleApiClient.connect()
    }

    private fun getNearByWifiList(gpsStatus: Boolean) {

        if (gpsStatus) {

            val getHotSpotRequest: GetHotSpotRequest = GetHotSpotRequest(
                lastKnownLocation!!.latitude,
                lastKnownLocation!!.longitude, "", true
            )
            mViewModel.getHotSpotResponse(getHotSpotRequest)
        } else {
            val getHotSpotRequest: GetHotSpotRequest = GetHotSpotRequest(
                Constants.LATITUDE,
                Constants.LONGITUDE, "", false
            )
            mViewModel.getHotSpotResponse(getHotSpotRequest)
        }

    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map


        // Prompt the user for permission.
        getLocationPermission()

        setUpClickListener()
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()
        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    private fun setUpClickListener() {
        this.map?.setOnMapClickListener(OnMapClickListener {
            mBinding.customPop.cvMainLayout.visibility = View.GONE
        })

//      Searchbar
        mBinding.toolbarLayout.etSearchBar.setOnClickListener {
            replaceFragment(SearchListFragment(), true)
        }
    }

    private fun getDeviceLocation() {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        if (requireActivity().checkLocSaveState()) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), HomeFragment.DEFAULT_ZOOM.toFloat()
                                )
                            )
                        } else {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        Constants.LATITUDE,
                                        Constants.LONGITUDE
                                    ), HomeFragment.DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }

                        getNearByWifiList(requireActivity().checkLocSaveState())
                    }
                } else {
                    map?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                Constants.LATITUDE,
                                Constants.LONGITUDE
                            ), HomeFragment.DEFAULT_ZOOM.toFloat()
                        )
                    )

                    getNearByWifiList(requireActivity().checkLocSaveState())

                }
            }

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun setupClusters() {
        if (this::mClusterManager.isInitialized) {
            mClusterManager.markerCollection.clear()
            mClusterManager.clusterMarkerCollection.clear()
        }
        mClusterManager = ClusterManager(activity, map)
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        map?.setOnCameraIdleListener(mClusterManager)
        map?.setOnMarkerClickListener(mClusterManager)
        val renderer = ClusterItemRenderer(requireActivity(), map, mClusterManager)
        mClusterManager.renderer = renderer
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    /* In this method  1. Showing  Custom Pop up window  along with toggle the mark as favourite option*/
    override fun onClusterItemClick(item: MyClusterItems?): Boolean {
        clickedVenueMarker = item;

        //Main cardview Layout
        mBinding.customPop.cvMainLayout.visibility = View.VISIBLE

        //Wifi Ssid
        mBinding.customPop.tvWifiSsid.text = clickedVenueMarker?.title
        mBinding.customPop.tvServiceProvider.text = clickedVenueMarker?.mNavigateURL
        mBinding.customPop.tvLocation.text = clickedVenueMarker?.mAddress
        if (clickedVenueMarker?.mIsFavourite == true) {
            mBinding.customPop.ivFavourites.setImageResource(R.drawable.ic_favorite_filled_red)
        } else {
            mBinding.customPop.ivFavourites.setImageResource(R.drawable.ic_favorite_filled_gray)
        }

        //Favourite
        mBinding.customPop.ivFavourites.setOnClickListener {
            if (HotSpotApp.prefs?.getAppLoggedInStatus()!!) {

                val imgID1: Drawable.ConstantState? =
                    requireContext().getDrawable(R.drawable.ic_favorite_filled_gray)
                        ?.getConstantState()
                val imgID2: Drawable.ConstantState? =
                    mBinding.customPop.ivFavourites.getDrawable().getConstantState()
                if (imgID1 == imgID2) {
                    mBinding.customPop.ivFavourites.setImageResource(R.drawable.ic_favorite_filled_red)
                    favouriteType = true
                } else {
                    mBinding.customPop.ivFavourites.setImageResource(R.drawable.ic_favorite_filled_gray)
                    favouriteType = false
                }
                val markFavouriteRequest: MarkFavouriteRequest =
                    MarkFavouriteRequest(clickedVenueMarker!!.mItemID)
                mViewModel.markFavouriteItem(markFavouriteRequest, favouriteType, item)

            } else {
                dialog.show()
            }
        }
        //Navigate Now
        mBinding.customPop.btnNavigateNow.setOnClickListener {

            val gmmIntentUri =
                Uri.parse("google.navigation:q=" + clickedVenueMarker?.mLat + "," + clickedVenueMarker?.mLng + "&mode=b")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        //Rate Now
        mBinding.customPop.btnRateNow.setOnClickListener {
            if (HotSpotApp.prefs?.getAppLoggedInStatus()!!) {

                val wifiProvideer: String = clickedVenueMarker?.title!!
                replaceFragment(
                    fragment = ReviewsFragment.newInstance(
                        locationId = clickedVenueMarker!!.mItemID,
                        wifiSsid = clickedVenueMarker!!.mNavigateURL,
                        wifiProvider = wifiProvideer,
                        location = clickedVenueMarker!!.mAddress
                    ),
                    addToBackStack = true
                )
            } else
                dialog.show()
        }

        //Report
        mBinding.customPop.ivFlag.setOnClickListener {
            if (HotSpotApp.prefs?.getAppLoggedInStatus()!!) {

                val wifiProvideer: String = clickedVenueMarker?.title!!
                replaceFragment(
                    RaiseComplaintFragment.newInstance(
                        locationId = clickedVenueMarker!!.mItemID,
                        wifiSsid = clickedVenueMarker!!.mNavigateURL,
                        wifiProvider = wifiProvideer,
                        location = clickedVenueMarker!!.mAddress
                    ), true
                )
            } else dialog.show()
        }

        mBinding.customPop.ivShare.setOnClickListener {


        }
        return false
    }

    /**
     * Method to add items in clusters
     */
    private fun setUpHotSpotDataInCluster(list: List<GetHotSpotResponse>) {
        for (location in list) {
            val offsetItems =
                MyClusterItems(
                    lat = location.lat.parseToDouble(),
                    lng = location.lng.parseToDouble(),
                    navigateURL = location.provider_name,
                    title = location.name,
                    snippet = location.navigate_url,
                    isfavourite = location.favourite,
                    itemId = location.id,
                    address = location.location
                )
            mClusterManager.addItem(offsetItems)
            mClusterManager.cluster()
        }
    }

    override fun onClusterItemInfoWindowClick(item: MyClusterItems?) {
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 12
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    class ClusterItemRenderer(
        context: Context, map: GoogleMap?,
        clusterManager: ClusterManager<MyClusterItems>
    ) : DefaultClusterRenderer<MyClusterItems>(context, map, clusterManager) {
        override fun onBeforeClusterItemRendered(
            item: MyClusterItems,
            markerOptions: MarkerOptions
        ) {
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_wifi_signal)
            markerOptions.icon(icon)
        }



    }

    override fun onConnected(p0: Bundle?) {
        mGoogleApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.interval = 30 * 1000.toLong()
        mLocationRequest!!.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(true)
        result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result!!.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    // Do something
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    try {
                        status.startResolutionForResult(requireActivity(), REQUEST_LOCATION)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    // Do something
                }
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActivityResult()", Integer.toString(resultCode))
        when (requestCode) {
            REQUEST_LOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d("location", "Location enabled")
                }
                Activity.RESULT_CANCELED -> {
                    Log.d("location", "Location not enabled, user canceled")
                }
                else -> {
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                HomeFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            HomeFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                } else {

                }
            }
        }
        updateLocationUI()
    }
}