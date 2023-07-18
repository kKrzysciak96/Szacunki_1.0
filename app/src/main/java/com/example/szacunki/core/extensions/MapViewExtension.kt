package com.example.szacunki.core.extensions

import android.content.Context
import android.location.Location
import com.example.szacunki.features.map.presentation.custom.CustomInfoWindow
import com.example.szacunki.features.map.presentation.custom.CustomMarker
import com.example.szacunki.features.map.presentation.model.GeoNoteDisplayable
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*

fun MapView.setMapConfigurations() {
    setMultiTouchControls(true)
    this.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
    val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this)
    locationOverlay.enableMyLocation()
    this.overlays.add(locationOverlay)

    val copyrightNotice: String = this.tileProvider.tileSource.copyrightNotice
    val copyrightOverlay = CopyrightOverlay(this.context)
    copyrightOverlay.setCopyrightNotice(copyrightNotice)
    this.overlays.add(copyrightOverlay)
}

fun MapView.refreshLocation() {
    overlayManager.forEach { predicate ->
        if (predicate is MyLocationNewOverlay) {
            predicate.apply { enableMyLocation() }
        }
    }
}

fun MapView.goToPosition(location: Location?) {
    this.controller.animateTo(
        GeoPoint(
            location?.latitude ?: 0.0, location?.longitude ?: 0.0
        ), if (this.zoomLevelDouble < 15) 15.0 else this.zoomLevelDouble, 500L
    )
}

fun MapView.goToPosition(geoPoint: GeoPoint?) {
    this.controller.animateTo(
        GeoPoint(
            geoPoint?.latitude ?: 0.0, geoPoint?.longitude ?: 0.0
        ), if (this.zoomLevelDouble < 15) 15.0 else this.zoomLevelDouble, 500L
    )
}

fun MapView.goToPosition(geoPoint: GeoPoint?, zoom: Double) {
    this.controller.animateTo(
        GeoPoint(
            geoPoint?.latitude ?: 0.0, geoPoint?.longitude ?: 0.0
        ), zoom, 500L
    )
}

fun MapView.createCustomMarker(
    geoNote: GeoNoteDisplayable, onLongPress: (GeoNoteDisplayable) -> Unit
) = CustomMarker(this) { onLongPress(geoNote) }

fun MapView.createCustomWindow(
    geoNote: GeoNoteDisplayable, onLongInfoWindowListener: (UUID) -> Unit
) = CustomInfoWindow(
    id = geoNote.id,
    title = geoNote.title + "," + " " + "Oddział" + " " + geoNote.section,
    description = geoNote.note,
    mapView = this@createCustomWindow,
    onLongInfoWindowListener = onLongInfoWindowListener
)

fun MapView.removeOldMarker(geoNote: GeoNoteDisplayable) {
    this.overlays.forEach { predicate ->
        if (predicate is CustomMarker) {
            if (predicate.id == geoNote.id.toString()) {
                predicate.closeInfoWindow()
                this.overlays.remove(predicate)
            }
        }
    }
}

fun MapView.removeALLOldMarkers() {
    this.overlays.forEach { predicate ->
        if (predicate is CustomMarker) {
            predicate.closeInfoWindow()
            this.overlays.remove(predicate)
        }
    }
}

fun MapView.addMarker(marker: Marker) {
    overlays.add(marker)
}

fun MapView.addMarkerToMap(
    context: Context,
    geoNote: GeoNoteDisplayable,
    onLongPress: (GeoNoteDisplayable) -> Unit,
    onLongPressInfoWindowListener: (UUID) -> Unit
) {
    val marker = this.createCustomMarker(geoNote, onLongPress)
    marker.adjustCustomMarker(this, geoNote, context, onLongPressInfoWindowListener)
    this.addMarker(marker)
    invalidate()
}

fun MapView.addPinFeature(longPressHelperListener: (GeoPoint?) -> Unit) {

    val eventReceiver = object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            return false
        }

        override fun longPressHelper(p: GeoPoint?): Boolean {
            longPressHelperListener(p)
            return true
        }
    }
    overlays.add(MapEventsOverlay(eventReceiver))
}







