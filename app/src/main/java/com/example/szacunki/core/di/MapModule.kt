package com.example.szacunki.core.di

import com.example.szacunki.features.map.data.MapRepositoryImpl
import com.example.szacunki.features.map.data.SharedPreferencesRepositoryImpl
import com.example.szacunki.features.map.domain.MapRepository
import com.example.szacunki.features.map.domain.SharedPreferencesRepository
import com.example.szacunki.features.map.presentation.screen.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {
    factory<MapRepository> { MapRepositoryImpl(get()) }
    factory<SharedPreferencesRepository> { SharedPreferencesRepositoryImpl(androidContext()) }
    viewModel { MapViewModel(get(), get(), get()) }
}