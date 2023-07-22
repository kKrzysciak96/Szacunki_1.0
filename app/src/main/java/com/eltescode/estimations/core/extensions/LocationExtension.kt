package com.eltescode.estimations.core.extensions

import android.location.Location
import org.osmdroid.util.GeoPoint

fun Location.toGeoPoint() = GeoPoint(this)
