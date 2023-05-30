package com.example.szacunki.core.app

import android.app.Application
import com.example.szacunki.core.di.koinInjector
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@KoinApp)
            modules(koinInjector)
        }
    }
}