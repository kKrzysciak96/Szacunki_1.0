package com.eltescode.estimations.features.map.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltescode.estimations.features.map.domain.model.GeoNoteDomain
import java.util.*

@Entity
data class GeoNoteCached(
    @PrimaryKey
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
