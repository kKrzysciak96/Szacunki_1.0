package com.eltescode.estimations.core.extensions

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.eltescode.estimations.features.map.presentation.model.GeoNoteDisplayable
import com.eltescode.estimations.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.*

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