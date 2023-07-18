package com.example.szacunki.features.map.presentation.model

import com.example.szacunki.features.map.domain.model.GeoNoteDomain
import java.util.*

data class GeoNoteDisplayable(
    val id: UUID,
    val section: String,
    val title: String,
    val note: String,
    val date: Date,
    val latitude: Double,
    val longitude: Double
) {
    constructor(geoNoteDomain: GeoNoteDomain) : this(
        id = geoNoteDomain.id,
        section = geoNoteDomain.section,
        title = geoNoteDomain.title,
        note = geoNoteDomain.note,
        date = geoNoteDomain.date,
        longitude = geoNoteDomain.longitude,
        latitude = geoNoteDomain.latitude
    )

    fun toGeoNoteDomain() = GeoNoteDomain(
        id = id,
        section = section,
        title = title,
        note = note,
        date = date,
        longitude = longitude,
        latitude = latitude
    )
}
