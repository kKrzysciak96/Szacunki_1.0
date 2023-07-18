package com.example.szacunki.features.map.presentation.screen

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szacunki.core.gps.GpsStateProvider
import com.example.szacunki.core.location.LocationProvider
import com.example.szacunki.features.map.domain.MapRepository
import com.example.szacunki.features.map.domain.SharedPreferencesRepository
import com.example.szacunki.features.map.presentation.model.CameraState
import com.example.szacunki.features.map.presentation.model.GeoNoteDisplayable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import java.util.*

class MapViewModel(
    private val mapRepository: MapRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val locationProvider: LocationProvider,
    private val gpsStateProvider: GpsStateProvider
) : ViewModel() {

    private val _cameraState = MutableStateFlow<CameraState>(CameraState(0.0, 0.0, 15.0))
    val cameraState = _cameraState.asStateFlow()

    private var _locationToZoomLocation = MutableStateFlow<GeoPoint?>(null)
    val locationToZoomLocation = _locationToZoomLocation.asStateFlow()

    //    val geoNotes = getAllGeoNotes()
    private val _geoNotes = MutableStateFlow<List<GeoNoteDisplayable>>(emptyList())
    val geoNotes = _geoNotes.asStateFlow()

    private val _isGpsEnabled = MutableStateFlow(false)
    val isGpsEnabled = _isGpsEnabled.asStateFlow()

    private val _currentLocation = MutableStateFlow<Location>(Location(""))
    val currentLocation = _currentLocation.asStateFlow()


    init {
        getAllGeoNotesState()
        loadCameraState()
        getCurrentLocation()
        getGpsStatus()
    }

    fun updateCameraState(latitude: Double, longitude: Double, zoom: Double) {
        _cameraState.update { CameraState(latitude, longitude, zoom) }
    }

    fun updateLocationToZoom(geoPoint: GeoPoint?) {
        _locationToZoomLocation.update { geoPoint }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                locationProvider.getCurrentLocation(5000).collect {
                    _currentLocation.value = it
                }
            }
        }
    }

    private fun getGpsStatus() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gpsStateProvider.isGPSEnabled(3000).collect {
                    _isGpsEnabled.value = it
                }
            }
        }
    }


    private fun loadCameraState() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cameraState = sharedPreferencesRepository.loadCameraState()
                val location = Location("")
                location.latitude = cameraState.latitude
                location.longitude = cameraState.longitude
                _cameraState.update { cameraState }
                _locationToZoomLocation.update {
                    GeoPoint(
                        cameraState.latitude,
                        cameraState.longitude
                    )
                }
            }
        }
    }

    private fun saveCameraState() {
        GlobalScope.launch {// Stwórz sobie globalnego scopa dla swojej aplikacji(koin) lub poczytaj o co kaman z tym GlobalScopem chodzi
            withContext(Dispatchers.IO) {
                sharedPreferencesRepository.saveCameraState(
                    CameraState(
                        cameraState.value.latitude,
                        cameraState.value.longitude,
                        cameraState.value.zoom
                    )
                )
            }
        }
    }

    private fun getAllGeoNotesState() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mapRepository.getAllGeoNotesFromLocal()
                    .map { geoNotesList -> geoNotesList.map { GeoNoteDisplayable(it) } }.collect {
                        _geoNotes.value = it
                    }

            }
        }
    }

    private fun getAllGeoNotes() = mapRepository.getAllGeoNotesFromLocal()
        .map { geoNotesList -> geoNotesList.map { GeoNoteDisplayable(it) } }

    fun saveGeoNoteToLocal(geoNote: GeoNoteDisplayable) {
        viewModelScope.launch { mapRepository.saveGeoNoteToLocal(geoNote.toGeoNoteDomain()) }
    }

    fun updateGeoNote(geoNote: GeoNoteDisplayable) {
        viewModelScope.launch { mapRepository.updateGeoNote(geoNote.toGeoNoteDomain()) }
    }

    private fun dropDataBase() {
        viewModelScope.launch { mapRepository.dropDataBase() }
    }

    fun deleteGeoNote(id: UUID) {
        viewModelScope.launch { withContext(Dispatchers.IO) { mapRepository.deleteGeoNote(id) } }
    }

    fun onDestroyActivity() {
        _locationToZoomLocation.update {
            GeoPoint(
                cameraState.value.latitude,
                cameraState.value.longitude
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        saveCameraState()
        Log.d("VIEWMODEL", "CLEARED")
    }


}
