package com.example.szacunki.core.gps

import android.location.LocationManager
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class GpsStateProviderImpl(private val locationManager: LocationManager) : GpsStateProvider {
    override fun isGPSEnabled(interval: Long): Flow<Boolean> {
        return flow {
            while (currentCoroutineContext().isActive) {
                emit(
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                )
                delay(interval)
            }
        }
    }
}