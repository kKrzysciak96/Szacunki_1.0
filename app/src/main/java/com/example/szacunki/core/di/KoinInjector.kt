package com.example.szacunki.core.di

import org.koin.core.module.Module

val koinInjector = listOf<Module>(
    appModule,
    dataBaseModule,
    estimationModule,
    mapModule
)