package com.example.szacunki.features.map.domain

interface SharedPreferencesRepository {
    suspend fun saveLastLocation(latitude: Double, longitude: Double)
    suspend fun loadLastLocation(): Pair<Double, Double>
}