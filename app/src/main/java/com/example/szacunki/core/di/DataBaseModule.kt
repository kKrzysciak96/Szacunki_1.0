package com.example.szacunki.core.di

import androidx.room.Room
import com.example.szacunki.core.database.EstimationDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), EstimationDataBase::class.java, "data-base")
            .fallbackToDestructiveMigration().build()
    }
    factory { get<EstimationDataBase>().provideEstimationDao() }
}