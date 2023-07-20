package com.example.szacunki.core.extensions

import android.location.Location
import org.osmdroid.util.GeoPoint

fun GeoPoint.toLocation(): Location {
    val location = Location("")
    location.latitude = latitude
    location.longitude = longitude
    return location
}