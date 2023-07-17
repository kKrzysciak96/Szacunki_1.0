package com.example.szacunki.core.extensions

import android.content.Context
import android.location.Location
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.example.szacunki.R
import com.example.szacunki.features.map.presentation.model.GeoNoteDisplayable
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.library.R.layout.bonuspack_bubble
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*

fun MapView.setMapConfigurations() {

    setMultiTouchControls(true)
    this.zoomController.setVisibility(
        CustomZoomButtonsController.Visibility.NEVER
    )
    val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), this);
    locationOverlay.enableMyLocation();
    this.overlays.add(locationOverlay)


    //Copyright overlay
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

fun Marker.adjustCustomMarker(
    mapView: MapView,
    geoNote: GeoNoteDisplayable,
    context: Context,
    onLongInfoWindowListener: (UUID) -> Unit
) {
    this.apply {
        position = GeoPoint(geoNote.latitude, geoNote.longitude)
        icon = AppCompatResources.getDrawable(context, R.drawable.ic_location)
        title = geoNote.title
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        this.id = geoNote.id.toString()
        infoWindow =
            mapView.createCustomWindow(geoNote = geoNote, onLongInfoWindowListener)

    }
}

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

class CustomMapListener(val listener: () -> Unit) : MapListener {
    override fun onScroll(event: ScrollEvent?): Boolean {
        listener()
        return false
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }
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

class CustomInfoWindow(
    private val title: String,
    private val description: String,
    private val id: UUID,
    mapView: MapView,
    private val onLongInfoWindowListener: (UUID) -> Unit
) : InfoWindow(bonuspack_bubble, mapView) {
    lateinit var windowTitle: TextView
    lateinit var windowDescription: TextView
    override fun onOpen(item: Any?) {
        mView.apply {
            windowTitle = findViewById(org.osmdroid.library.R.id.bubble_title)
            windowDescription = findViewById(org.osmdroid.library.R.id.bubble_description)
        }
        windowTitle.text = title
        windowDescription.text = description
        mView.setOnLongClickListener {
            onLongInfoWindowListener(id)
            true
        }
        mView.setOnClickListener {
            this.close()
        }
    }
    override fun onClose() {
    }
}

