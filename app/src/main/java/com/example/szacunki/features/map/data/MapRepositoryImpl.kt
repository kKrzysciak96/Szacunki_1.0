package com.example.szacunki.features.map.data

import com.example.szacunki.features.map.data.model.GeoNoteCached
import com.example.szacunki.features.map.domain.MapRepository
import com.example.szacunki.features.map.domain.model.GeoNoteDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class MapRepositoryImpl(private val geoNoteDao: GeoNoteDao) : MapRepository {
    override fun getAllGeoNotesFromLocal(): Flow<List<GeoNoteDomain>> {
        return geoNoteDao.getAllGeoNotesFromLocal()
            .map { geoNoteDomainList -> geoNoteDomainList.map { it.toGeoNoteDomain() } }
    }

    override fun getSingleGeoNoteFromLocal(id: UUID): Flow<GeoNoteDomain> {
        return geoNoteDao.getSingleGeoNoteFromLocal(id).map { it.toGeoNoteDomain() }
    }

    override suspend fun saveGeoNoteToLocal(geoNoteDomain: GeoNoteDomain) {
        geoNoteDao.saveGeoNoteToLocal(GeoNoteCached(geoNoteDomain))
    }

    override suspend fun updateGeoNote(geoNoteDomain: GeoNoteDomain) {
        geoNoteDao.updateGeoNote(GeoNoteCached(geoNoteDomain))
    }

    override suspend fun deleteGeoNote(id: UUID) {
        geoNoteDao.deleteGeoNote(id)
    }

    override suspend fun dropDataBase() {
        geoNoteDao.dropDataBase()
    }
}