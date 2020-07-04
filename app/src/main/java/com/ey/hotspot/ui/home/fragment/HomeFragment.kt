package com.ey.hotspot.ui.home.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentHomeBinding
import com.ey.hotspot.ui.home.models.MyClusterItems
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
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


        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
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
        map?.setOnCameraIdleListener(mClusterManager)
        map?.setOnMarkerClickListener(mClusterManager)


        val renderer = ClusterItemRenderer(requireActivity(), map, mClusterManager)
        mClusterManager.renderer = renderer

        setUpMarkerInfoWindows()


        addItems()
    }


    private fun setUpMarkerInfoWindows() {


        mClusterManager.markerCollection.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(p0: Marker?): View {
                TODO("Not yet implemented")
            }

            override fun getInfoWindow(p0: Marker?): View {
                val inflater =
                    requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view: View = inflater.inflate(R.layout.custom_marker_info_window, null)

                val navigateButton =
                    view.findViewById<View>(R.id.btNavigate) as AppCompatButton

                val shareIt = view.findViewById<View>(R.id.ivShareIT) as ImageView


                //TODO  Remaining click listener
                shareIt.setOnClickListener {

                    Toast.makeText(activity, "Its toast!", Toast.LENGTH_SHORT).show()

                }
                navigateButton.setOnClickListener {

                    Toast.makeText(activity, "Its toast!", Toast.LENGTH_SHORT).show()


                }
                return view
            }

        })
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
                }
            }
        }
        updateLocationUI()
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


    override fun onClusterClick(cluster: Cluster<MyClusterItems>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<MyClusterItems>?) {
        TODO("Not yet implemented")
    }

    override fun onClusterItemClick(item: MyClusterItems?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onClusterItemInfoWindowClick(item: MyClusterItems?) {
        TODO("Not yet implemented")
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

}