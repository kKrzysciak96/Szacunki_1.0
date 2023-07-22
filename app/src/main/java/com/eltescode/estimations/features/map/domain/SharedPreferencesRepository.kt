package com.eltescode.estimations.features.map.domain

import com.eltescode.estimations.features.map.presentation.custom.CameraState

interface SharedPreferencesRepository {
    suspend fun saveCameraState(cameraState: CameraState)
    suspend fun loadCameraState(): CameraState
}