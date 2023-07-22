package com.eltescode.estimations.features.map.presentation.custom

import android.widget.TextView
import org.osmdroid.library.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.util.*

class CustomInfoWindow(
    private val title: String,
    private val description: String,
    private val id: UUID,
    mapView: MapView,
    private val onLongInfoWindowListener: (UUID) -> Unit
) : InfoWindow(R.layout.bonuspack_bubble, mapView) {
    private lateinit var windowTitle: TextView
    private lateinit var windowDescription: TextView

    override fun onOpen(item: Any?) {
        mView.apply {
            windowTitle = findViewById(R.id.bubble_title)
            windowDescription = findViewById(R.id.bubble_description)
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