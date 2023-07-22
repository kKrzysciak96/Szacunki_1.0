package com.eltescode.estimations.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eltescode.estimations.features.estimation.data.local.model.EstimationCached
import com.eltescode.estimations.features.estimation.data.local.model.EstimationDao
import com.eltescode.estimations.features.map.data.GeoNoteDao
import com.eltescode.estimations.features.map.data.model.GeoNoteCached

@Database(
    entities = [EstimationCached::class, GeoNoteCached::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class, TreeRowCachedConverter::class, TreeCachedConverter::class)
abstract class EstimationDataBase : RoomDatabase() {
    abstract fun provideEstimationDao(): EstimationDao
    abstract fun provideGeoNoteDao(): GeoNoteDao
}