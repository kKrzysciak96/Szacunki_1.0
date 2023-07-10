package com.example.szacunki.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.szacunki.features.estimation.data.local.model.EstimationCached
import com.example.szacunki.features.estimation.data.local.model.EstimationDao
import com.example.szacunki.features.map.data.GeoNoteDao
import com.example.szacunki.features.map.data.model.GeoNoteCached

@Database(
    entities = [EstimationCached::class, GeoNoteCached::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, TreeRowCachedConverter::class, TreeCachedConverter::class)
abstract class EstimationDataBase : RoomDatabase() {
    abstract fun provideEstimationDao(): EstimationDao
    abstract fun provideGeoNoteDao(): GeoNoteDao
}