package com.example.szacunki.core.location

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class CurrentLocationProviderImpl(var locationManager: LocationManager) : CurrentLocationProvider {

    private var currentLocation: Location? = null


    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Location {
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val gpsLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                currentLocation = location
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        if (hasGps) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                gpsLocationListener
            )
        }

        val lastKnownLocationByGps =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocationByGps?.let {
            currentLocation = lastKnownLocationByGps
        }

        return currentLocation ?: Location("")
    }


}