package com.example.szacunki.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.szacunki.features.estimation.data.local.model.EstimationCached
import com.example.szacunki.features.estimation.data.local.model.EstimationDao

@Database(entities = [EstimationCached::class], version = 1)
@TypeConverters(DateConverter::class, TreeRowCachedConverter::class, TreeCachedConverter::class)
abstract class EstimationDataBase : RoomDatabase() {
    abstract fun provideEstimationDao(): EstimationDao
}