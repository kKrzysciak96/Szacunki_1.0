package com.example.szacunki.core.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getCurrentLocationFlow(interval: Long): Flow<Location>
    class LocationException(message: String) : Exception()
}