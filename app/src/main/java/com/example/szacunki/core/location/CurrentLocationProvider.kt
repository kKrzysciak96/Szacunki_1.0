package com.example.szacunki.core.location

import android.location.Location

interface CurrentLocationProvider {

    fun getCurrentLocation(): Location

}