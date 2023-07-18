package com.example.szacunki.core.di

import android.content.Context
import android.location.LocationManager
import com.example.szacunki.core.gps.GpsStateProvider
import com.example.szacunki.core.gps.GpsStateProviderImpl
import com.example.szacunki.core.location.LocationProvider
import com.example.szacunki.core.location.LocationProviderImpl
import com.example.szacunki.core.screen.general.GeneralViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    factory { LocationServices.getFusedLocationProviderClient(androidContext()) }
    factory { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    factory<LocationProvider> { LocationProviderImpl(get(), get(), get()) }
    factory<GpsStateProvider> { GpsStateProviderImpl(get()) }
    viewModel { GeneralViewModel() }

}