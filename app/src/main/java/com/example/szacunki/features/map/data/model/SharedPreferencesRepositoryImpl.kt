package com.example.szacunki.features.map.data.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.szacunki.features.map.domain.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl(private val context: Context) : SharedPreferencesRepository {
    override suspend fun saveLastLocation(latitude: Double, longitude: Double) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("location", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putFloat(LATITUDE_KEY, latitude.toFloat())
            putFloat(LONGITUDE_KEY, longitude.toFloat())
        }.apply()
        Log.d("SHARED", "Zapisano: $latitude, $longitude")
    }

    override suspend fun loadLastLocation(): Pair<Double, Double> {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("location", Context.MODE_PRIVATE)
        val savedLatitude = sharedPreferences.getFloat(LATITUDE_KEY, 0f).toDouble()
        val savedLongitude = sharedPreferences.getFloat(LONGITUDE_KEY, 0f).toDouble()
        Log.d("SHARED", "Wczytano: $savedLatitude, $savedLongitude")
        return savedLatitude to savedLongitude

    }

    companion object {
        const val LATITUDE_KEY = "LATITUDE_KEY"
        const val LONGITUDE_KEY = "LONGITUDE_KEY"
    }
}