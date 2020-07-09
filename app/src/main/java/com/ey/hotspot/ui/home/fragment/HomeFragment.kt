package com.ey.hotspot.ui.home.fragment

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentHomeBinding
import com.ey.hotspot.ui.home.models.MyClusterItems
import com.ey.hotspot.ui.search.recentlysearch.RecentlySearchFragment
import com.ey.hotspot.utils.checkLocationPermission
import com.ey.hotspot.utils.isLocationEnabled
import com.ey.hotspot.utils.replaceFragment
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
    private var cameraPosition: CameraPosition? = null

    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null

    private lateinit var mClusterManager: ClusterManager<MyClusterItems>
    private var clickedVenueMarker: MyClusterItems? = null


    private val points = ArrayList<LatLng>()


    override fun getLayoutId(): Int {

        return R.layout.fragment_home


    }

    override fun getViewModel(): Class<HomeFragmentViewModel> {

        return HomeFragmentViewModel::class.java
    }

    override fun onBinding() {

        //Toolbar
        setUpSearchBar(mBinding.toolbarLayout, showUpButton = false, enableSearchButton = false){}

        mBinding.toolbarLayout.etSearchBar.isFocusable = false

        // Prompt the user for permission.

        activity?.checkLocationPermission(view = mBinding.root, func = {
            locationPermissionGranted = true
            if (!requireActivity().isLocationEnabled()) {
                checkGPSEnable()
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
            mBinding.customPop.llCustomPopMain.visibility = View.GONE
        })

//        Searchbar
        mBinding.toolbarLayout.etSearchBar.setOnClickListener {
            replaceFragment(RecentlySearchFragment(), true)
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
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), HomeFragment.DEFAULT_ZOOM.toFloat()
                                )
                            )

                            setupClusters()
                            showNearbyWifis(points);


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

        addItems()
    }

    private fun addItems() {


        var lat = lastKnownLocation!!.latitude
        var lng = lastKnownLocation!!.longitude
        for (i in 0..9) {
            val offset = i / 60.0
            lat = lat + offset
            lng = lng + offset
            val title = "Wifi Name  +$i"
            val snippet = ""
            val offsetItem = MyClusterItems(lat, lng, title, snippet)
            mClusterManager.addItem(offsetItem)
        }


        var latOne = lastKnownLocation!!.latitude + 1.0
        var lngOne = lastKnownLocation!!.longitude + 1.0
        for (i in 0..9) {


            val offset = i / 60.0
            lat = latOne + offset
            lng = lngOne + offset
            val title = "Wifi Name  +$i"
            val snippet = ""
            val offsetItem = MyClusterItems(lat, lng, title, snippet)
            mClusterManager.addItem(offsetItem)
        }

        var latTwo = lastKnownLocation!!.latitude + 1.0
        var lngTwo = lastKnownLocation!!.longitude + 1

        for (i in 0..9) {

            val offset = i / 60.0
            lat = latTwo + offset
            lng = lngTwo + offset
            val title = "Wifi Name  +$i"
            val snippet = ""
            val offsetItem = MyClusterItems(lat, lng, title, snippet)
            mClusterManager.addItem(offsetItem)

        }


    }


    private fun showNearbyWifis(points: ArrayList<LatLng>) {
        val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_wifi_signal)


        for (location in points) {

            map?.addMarker(
                MarkerOptions().position(location)
                    .icon(icon)
                    .title("Nashik")
            )
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

        Log.d("Cluster", "onClusterClick")
        return true
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<MyClusterItems>?) {

        Log.d("Cluster", "onClusterInfoWindowClick")

    }

    override fun onClusterItemClick(item: MyClusterItems?): Boolean {

        Log.d("Cluster", "onClusterItemClick")

        clickedVenueMarker = item;

        mBinding.customPop.llCustomPopMain.visibility = View.VISIBLE

        mBinding.customPop.tvWifiNameTitle.setText(clickedVenueMarker?.title)



        mBinding.customPop.tvWifiNameTitle.setOnClickListener {

            val dummy: String = "something"
            Toast.makeText(requireActivity(), "HELLO", Toast.LENGTH_SHORT).show()
        }

        mBinding.customPop.btNavigate.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:18.520430,73.856743")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            // mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(requireActivity().packageManager)?.let {
                requireActivity().startActivity(mapIntent)
            }

        }



        return false
    }

    override fun onClusterItemInfoWindowClick(item: MyClusterItems?) {
        Log.d("Cluster", "onClusterItemInfoWindowClick")


    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 7
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

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


    private fun checkGPSEnable() {
        AlertDialog.Builder(requireContext())
            .setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
            .show()
    }
}