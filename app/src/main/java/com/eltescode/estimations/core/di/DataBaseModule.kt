package com.eltescode.estimations.core.di

import androidx.room.Room
import com.eltescode.estimations.core.database.EstimationDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), EstimationDataBase::class.java, "data-base")
            .build()
    }
    factory { get<EstimationDataBase>().provideEstimationDao() }
    factory { get<EstimationDataBase>().provideGeoNoteDao() }
}