package com.ey.hotspot.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager

class GPSCheck(private val locationCallBack: LocationCallBack) :
    BroadcastReceiver() {

    interface LocationCallBack {
        fun turnedOn()
        fun turnedOff()
    }

    override fun onReceive(context: Context, intent: Intent) {
        val locationManager =
            context.getSystemService(LOCATION_SERVICE) as LocationManager

        intent.action?.let {
            if (it == LocationManager.PROVIDERS_CHANGED_ACTION) {
                // Make an action or refresh an already managed state.
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    locationCallBack.turnedOn()
                else
                    locationCallBack.turnedOff()
            }
        }
    }
}