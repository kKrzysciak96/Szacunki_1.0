package com.example.szacunki.features.map.domain

import com.example.szacunki.features.map.presentation.model.CameraState

interface SharedPreferencesRepository {
    suspend fun saveCameraState(cameraState: CameraState)
    suspend fun loadCameraState(): CameraState
}