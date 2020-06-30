package com.ey.hotspot.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityHomeBinding
import com.ey.hotspot.ui.home.models.MyClusterItems
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.clustering.ClusterManager

class HomeActivity : BaseActivity<ActivityHomeBinding,HomeViewModel>(),OnMapReadyCallback {

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null

    private lateinit var mClusterManager: ClusterManager<MyClusterItems>


    private val points = ArrayList<LatLng>()


    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun getViewModel(): Class<HomeViewModel> {
       return HomeViewModel::class.java
    }

    override fun onBinding() {



        Places.initialize(applicationContext, getString(R.string.maps_api_key))
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

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
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )

                            setupClusters()
                            showNearbyWifis(points);


                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
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
        mClusterManager = ClusterManager(this, map)
        map?.setOnCameraIdleListener(mClusterManager)
        map?.setOnMarkerClickListener(mClusterManager)
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


        var latOne = lastKnownLocation!!.latitude+1.0
        var lngOne = lastKnownLocation!!.longitude+1.0
        for (i in 0..9) {


            val offset = i / 60.0
            lat = latOne + offset
            lng = lngOne + offset
            val title = "Wifi Name  +$i"
            val snippet = ""
            val offsetItem = MyClusterItems(lat, lng, title, snippet)
            mClusterManager.addItem(offsetItem)
        }

        var latTwo =lastKnownLocation!!.latitude+1.0
        var lngTwo = lastKnownLocation!!.longitude+1

        for(i in 0..9){

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


        for (location in points) {

            map?.addMarker(
                MarkerOptions().position(location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_wifi))
                    .alpha(0.7f).title("Nashik")

            )
        }
    }

    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
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
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.current_place_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_get_place -> {
                startActivity(Intent(this, ReviewAndComplainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        private val TAG = HomeActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 7
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

    }
}