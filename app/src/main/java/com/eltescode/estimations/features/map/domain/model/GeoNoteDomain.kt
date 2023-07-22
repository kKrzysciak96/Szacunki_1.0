package com.eltescode.estimations.features.map.domain.model

import java.util.*

data class GeoNoteDomain(
    val id: UUID,
    val section: String,
    val title: String,
    val note: String,
    val date: Date,
    val latitude: Double,
    val longitude: Double,
    )
