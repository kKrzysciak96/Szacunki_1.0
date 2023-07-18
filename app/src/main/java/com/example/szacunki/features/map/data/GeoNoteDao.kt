package com.example.szacunki.features.map.data

import androidx.room.*
import com.example.szacunki.features.map.data.model.GeoNoteCached
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface GeoNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGeoNoteToLocal(geoNote: GeoNoteCached)

    @Query("SELECT * FROM GeoNoteCached")
    fun getAllGeoNotesFromLocal(): Flow<List<GeoNoteCached>>

    @Query("SELECT * FROM GeoNoteCached WHERE id = :id")
    fun getSingleGeoNoteFromLocal(id: UUID): Flow<GeoNoteCached>

    @Query("DELETE FROM GeoNoteCached WHERE id = :id")
    fun deleteGeoNote(id: UUID)

    @Update
    suspend fun updateGeoNote(geoNote: GeoNoteCached)

    @Query("DELETE FROM GeoNoteCached")
    suspend fun dropDataBase()
}