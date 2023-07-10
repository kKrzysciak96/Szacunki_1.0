package com.example.szacunki.core.di

import android.content.Context
import android.location.LocationManager
import com.example.szacunki.core.location.*
import com.example.szacunki.core.screen.general.GeneralViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    factory<CurrentLocationProvider> { CurrentLocationProviderImpl(get()) }
    factory { LocationServices.getFusedLocationProviderClient(androidContext()) }
    factory<LocationProvider> { LocationProviderImpl(get(), get()) }
    factory<LocationProviderLast> { LocationProviderLastImpl(androidContext(), get()) }
    viewModel { GeneralViewModel() }

}