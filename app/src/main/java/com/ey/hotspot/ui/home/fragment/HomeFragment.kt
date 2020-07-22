package com.ey.hotspot.ui.home.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.FragmentHomeBinding
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.MyClusterItems
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.ui.search.searchlist.SearchListFragment
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.utils.*
import com.ey.hotspot.utils.constants.setUpSearchBar
import com.ey.hotspot.utils.dialogs.YesNoDialog
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
    ClusterManager.OnClusterClickListener<MyClusterItems>,
    ClusterManager.OnClusterInfoWindowClickListener<MyClusterItems>,
    ClusterManager.OnClusterItemClickListener<MyClusterItems>,
    ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItems>,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    private var map: GoogleMap? = null
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
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
                    goToLoginScreen()
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
        activity?.setUpSearchBar(mBinding.toolbarLayout, showUpButton = false, enableSearchButton = false) {}
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
            if (it.status) {
                setupClusters()
                setUpHotSpotDataInCluster(it.data)
            } else {
                showMessage(it.message, true)
            }
        })
        mViewModel.markFavouriteResponse.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                showMessage(it.message, true)
            } else {
                showMessage(it.message, true)
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

    private fun getNearByWifiList() {
        val getHotSpotRequest: GetHotSpotRequest = GetHotSpotRequest(
            19.1403509,
            72.8096671, "", true
        )
        mViewModel.getHotSpotResponse(getHotSpotRequest)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
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
//        Searchbar
        mBinding.toolbarLayout.etSearchBar.setOnClickListener {
            replaceFragment(SearchListFragment(), true)
        }
    }
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            if (checkLocSaveState()) {
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
                                            19.1431,
                                            72.8105
                                        ), HomeFragment.DEFAULT_ZOOM.toFloat()
                                    )
                                )
                            }
                            getNearByWifiList()
                        }
                    } else {
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, HomeFragment.DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    private fun setupClusters() {
        mClusterManager = ClusterManager(activity, map)
        mClusterManager.setOnClusterClickListener(this);
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
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    override fun onClusterClick(cluster: Cluster<MyClusterItems>?): Boolean {
        return true
    }
    override fun onClusterInfoWindowClick(cluster: Cluster<MyClusterItems>?) {
        //TODO
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
                mViewModel.markFavouriteItem(markFavouriteRequest, favouriteType)
            } else {
                dialog.show()
            }
        }
        //Navigate Now
        mBinding.customPop.btnNavigateNow.setOnClickListener {
                val url = clickedVenueMarker?.snippet
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
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
        return false
    }
    /**
     * Method to add items in clusters
     */
    private fun setUpHotSpotDataInCluster(list: List<GetHotSpotResponse>) {
        for (location in list) {
            val offsetItems =
                MyClusterItems(
                    location.lat.parseToDouble(),
                    location.lng.parseToDouble(),
                    location.provider_name,
                    location.navigate_url,
                    location.favourite,
                    location.name,
                    location.id,
                    location.location
                )
            mClusterManager.addItem(offsetItems)
        }
    }
    override fun onClusterItemInfoWindowClick(item: MyClusterItems?) {
        //TODO
    }
    companion object {
        private val TAG = HomeFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 12
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
        override fun onClusterItemUpdated(item: MyClusterItems, marker: Marker) {
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.ic_wifi_signal)
            marker.setIcon(icon)
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

    private fun goToLoginScreen() {
        //Clear Data
        HotSpotApp.prefs?.clearSharedPrefData()

        //Redirect user to Login Activity
        CoreApp.instance.startActivity(Intent(CoreApp.instance, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}