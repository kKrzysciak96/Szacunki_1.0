package com.example.szacunki.core.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getCurrentLocation(interval: Long): Flow<Location>

}