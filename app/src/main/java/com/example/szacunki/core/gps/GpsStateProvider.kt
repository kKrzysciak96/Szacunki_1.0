package com.example.szacunki.core.gps

import kotlinx.coroutines.flow.Flow

interface GpsStateProvider {
    fun isGPSEnabled(interval: Long): Flow<Boolean>
}