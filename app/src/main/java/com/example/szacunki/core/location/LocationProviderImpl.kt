package com.example.szacunki.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.example.szacunki.core.extensions.hasLocationPermission
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LocationProviderImpl(
    private val context: Context, private val client: FusedLocationProviderClient
) : LocationProvider {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun isGPSEnabled(): Flow<Boolean> {
        return flow {

            while (currentCoroutineContext().isActive) {
                emit(
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                )
                delay(3000)
            }
        }
    }

    //    fun isNetworkEnabled() = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    @SuppressLint("MissingPermission")
    override fun getCurrentLocationFlow(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
//                throw LocationProvider.LocationException("Missing location permission")
            }
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

//            if (!isGpsEnabled && !isNetworkEnabled) {
//                throw LocationProvider.LocationException("GPS is disabled")
//            }

            val locationRequest =
                LocationRequest.Builder(interval).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch {
                            Log.d("LOCATION", "WYSLAŁO")
                            send(location)
                        }
                    }
                }
            }
            client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

//    @SuppressLint("MissingPermission")
//    override fun getCurrentLocation(interval: Long): Location? {
//
//        if (!context.hasLocationPermission()) {
//            throw LocationProvider.LocationException("Missing location permission")
//        }
//        val locationManager =
//            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        val isNetworkEnabled =
//            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        if (!isGpsEnabled && !isNetworkEnabled) {
//            throw LocationProvider.LocationException("GPS is disabled")
//        }
//        val locationRequest = LocationRequest.Builder(interval).build()
//
//
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(result: LocationResult) {
//                super.onLocationResult(result)
//                result.locations.lastOrNull()?.let { location ->
//                    currentLocation = location
//                    Log.d("LOCATION", location.latitude.toString() + " " + location.longitude.toString())
//                    client.removeLocationUpdates(this)
//                } ?: Log.d("LOCATION", "null")
//            }
//        }
//        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
//
//
//        return currentLocation
//    }
}