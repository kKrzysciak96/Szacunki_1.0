package com.eltescode.estimations.core.di

import com.eltescode.estimations.features.map.data.MapRepositoryImpl
import com.eltescode.estimations.features.map.data.SharedPreferencesRepositoryImpl
import com.eltescode.estimations.features.map.domain.MapRepository
import com.eltescode.estimations.features.map.domain.SharedPreferencesRepository
import com.eltescode.estimations.features.map.presentation.screen.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {
    factory<MapRepository> { MapRepositoryImpl(get()) }
    factory<SharedPreferencesRepository> { SharedPreferencesRepositoryImpl(androidContext()) }
    viewModel { MapViewModel(get(), get(), get(), get(), get()) }
}