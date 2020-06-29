package com.ey.hotspot.ui.home.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentHomeBinding
import com.ey.hotspot.ui.home.HomeViewModel
import com.ey.hotspot.ui.home.models.MyClusterItems
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


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>() {



    private val points = ArrayList<LatLng>()


    override fun getLayoutId(): Int {

        return R.layout.fragment_home


    }

    override fun getViewModel(): Class<HomeFragmentViewModel> {

        return HomeFragmentViewModel::class.java
    }

    override fun onBinding() {


    }



}