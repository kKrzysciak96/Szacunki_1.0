package com.example.szacunki.features.map.presentation.custom

import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent

class CustomMapListener(val listener: () -> Unit) : MapListener {
    override fun onScroll(event: ScrollEvent?): Boolean {
        listener()
        return false
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        return false
    }
}