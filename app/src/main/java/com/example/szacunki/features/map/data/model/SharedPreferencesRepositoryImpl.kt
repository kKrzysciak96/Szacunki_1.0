package com.example.szacunki.features.map.data.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.szacunki.features.map.domain.SharedPreferencesRepository
import com.example.szacunki.features.map.presentation.model.CameraState

class SharedPreferencesRepositoryImpl(private val context: Context) : SharedPreferencesRepository {
    override suspend fun saveCameraState(cameraState: CameraState) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("location", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putFloat(LATITUDE_KEY, cameraState.latitude.toFloat())
            putFloat(LONGITUDE_KEY, cameraState.longitude.toFloat())
            putFloat(ZOOM_KEY, cameraState.zoom.toFloat())
        }.apply()
        Log.d(
            "SHARED",
            "Zapisano: ${cameraState.latitude}, ${cameraState.longitude}, ${cameraState.zoom}"
        )
    }

    override suspend fun loadCameraState(): CameraState {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("location", Context.MODE_PRIVATE)
        val savedLatitude = sharedPreferences.getFloat(LATITUDE_KEY, 0f).toDouble()
        val savedLongitude = sharedPreferences.getFloat(LONGITUDE_KEY, 0f).toDouble()
        val savedZoom = sharedPreferences.getFloat(ZOOM_KEY, 0f).toDouble()
        Log.d("SHARED", "Wczytano: $savedLatitude, $savedLongitude, $savedZoom")
        return CameraState(savedLatitude, savedLongitude, savedZoom)

    }

    companion object {
        const val LATITUDE_KEY = "LATITUDE_KEY"
        const val LONGITUDE_KEY = "LONGITUDE_KEY"
        const val ZOOM_KEY = "ZOOM_KEY"
    }
}