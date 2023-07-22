package com.eltescode.estimations.features.map.domain

import com.eltescode.estimations.features.map.domain.model.GeoNoteDomain
import kotlinx.coroutines.flow.Flow
import java.util.*

interface MapRepository {
    fun getAllGeoNotesFromLocal(): Flow<List<GeoNoteDomain>>
    fun getSingleGeoNoteFromLocal(id: UUID): Flow<GeoNoteDomain>
    suspend fun saveGeoNoteToLocal(geoNoteDomain: GeoNoteDomain)
    suspend fun updateGeoNote(geoNoteDomain: GeoNoteDomain)
    suspend fun deleteGeoNote(id: UUID)
    suspend fun dropDataBase()
}