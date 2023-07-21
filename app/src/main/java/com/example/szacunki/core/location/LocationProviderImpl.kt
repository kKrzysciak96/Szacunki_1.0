package com.example.szacunki.core.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class LocationProviderImpl(
    private val client: FusedLocationProviderClient,
) : LocationProvider {
    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(interval: Long): Flow<Location> {
        return callbackFlow {
            val locationRequest =
                LocationRequest.Builder(interval).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build()
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch {
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
}
