package com.ey.hotspot.ui.home.fragment

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.FragmentHomeBinding
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse
import com.ey.hotspot.ui.home.models.MyClusterItems
import com.ey.hotspot.ui.search.searchlist.SearchListFragment
import com.ey.hotspot.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    ClusterManager.OnClusterItemInfoWindowClickListener<MyClusterItems> {


    private var map: GoogleMap? = null

    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null
    private lateinit var mClusterManager: ClusterManager<MyClusterItems>
    private var clickedVenueMarker: MyClusterItems? = null


    private var favouriteType: Boolean = false


    override fun getLayoutId(): Int {

        return R.layout.fragment_home


    }

    override fun getViewModel(): Class<HomeFragmentViewModel> {

        return HomeFragmentViewModel::class.java
    }

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        //Toolbar
        setUpSearchBar(mBinding.toolbarLayout, showUpButton = false, enableSearchButton = false) {}

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
                setUpHotSpotData(it.data)

            } else {
                showMessage(it.message, true)
            }
        })


        mViewModel.getUserHotSpotResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {

                setupClusters()
                setUpUserHotSpotData(it.data)

            } else {
                showMessage(it.message, true)
            }
        })

        mViewModel.markFavouriteResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {

                showMessage(it.message, true)

            } else {
                showMessage(it.message, true)
            }
        })


    }

    private fun setUpUserHotSpotData(list: List<GetUserHotSpotResponse>) {


        for (location in list) {

            val offsetItems =
                MyClusterItems(
                    location.lat,
                    location.lng,
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

    private fun getNearByWifiList() {

        val getHotSpotRequest: GetHotSpotRequest = GetHotSpotRequest(
            19.1403509,
            72.8096671, "", true
        )

        if (HotSpotApp.prefs!!.getAppLoggedInStatus() == true) {

            mViewModel.getUserHotSpotResponse(getHotSpotRequest)
        } else {
            mViewModel.getHotSpotResponse(getHotSpotRequest)

        }

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
                            /*map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), HomeFragment.DEFAULT_ZOOM.toFloat()
                                )
                            )*/

                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        19.1431,
                                        72.8105
                                    ), HomeFragment.DEFAULT_ZOOM.toFloat()
                                )
                            )
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

    private fun setUpHotSpotData(getHotSpotResponse: List<GetHotSpotResponse>) {
        for (location in getHotSpotResponse) {

            val offsetItems =
                MyClusterItems(
                    location.lat,
                    location.lng,
                    location.provider_name,
                    location.navigate_url

                )
            mClusterManager.addItem(offsetItems)
        }
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

        mBinding.customPop.ivFavourites.setOnClickListener {

            val imgID1: Drawable.ConstantState? =
                requireContext().getDrawable(R.drawable.ic_favorite_filled_gray)?.getConstantState()

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

        }

        //Navigate Now
        mBinding.customPop.btnNavigateNow.setOnClickListener {
            val url = clickedVenueMarker?.snippet

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

        }
        return false
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
}