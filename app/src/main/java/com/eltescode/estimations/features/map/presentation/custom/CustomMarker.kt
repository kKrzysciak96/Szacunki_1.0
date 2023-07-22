package com.eltescode.estimations.features.map.presentation.custom

import android.view.MotionEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CustomMarker(mapView: MapView, val onLongMarkerPress: () -> Unit) : Marker(mapView) {

    override fun onLongPress(event: MotionEvent?, mapView: MapView?): Boolean {
        onLongMarkerPress()
        return super.onLongPress(event, mapView)
    }

    override fun onMarkerClickDefault(marker: Marker?, mapView: MapView?): Boolean {
        return super.onMarkerClickDefault(marker, mapView)
    }

    override fun onSingleTapUp(e: MotionEvent?, mapView: MapView?): Boolean {
        return super.onSingleTapUp(e, mapView)
    }

    override fun onSingleTapConfirmed(event: MotionEvent?, mapView: MapView?): Boolean {
        return super.onSingleTapConfirmed(event, mapView)
    }
}