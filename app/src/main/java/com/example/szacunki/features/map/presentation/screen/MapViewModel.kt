package com.example.szacunki.features.map.presentation.screen

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szacunki.core.location.LocationProvider
import com.example.szacunki.core.location.LocationProviderImpl
import com.example.szacunki.features.map.domain.MapRepository
import com.example.szacunki.features.map.domain.SharedPreferencesRepository
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
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _cameraState = MutableStateFlow(Location(""))
    val cameraState = _cameraState.asStateFlow()

    private var _clickedLocation = MutableStateFlow<GeoPoint?>(null)
    val clickedLocation = _clickedLocation.asStateFlow()

    private var _locationToZoomLocation = MutableStateFlow<GeoPoint?>(null)
    val locationToZoomLocation = _locationToZoomLocation.asStateFlow()

    val geoNotes = getAllGeoNotes()

    private val _isGpsEnabled = MutableStateFlow(false)
    val isGpsEnabled = _isGpsEnabled.asStateFlow()

    //    val _currentLocation = MutableStateFlow(Location(""))
    private val _currentLocation = MutableStateFlow<Location>(Location(""))
    val currentLocation = _currentLocation.asStateFlow()

    private var _lastLocation = MutableStateFlow<Location>(Location(""))
    val lastLocation = _lastLocation.asStateFlow()

    init {
        loadLastLocation()
        getLocation()
        getGpsStatus()
    }

    fun isGpsEnabled() = locationProvider

    private fun getLocation() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                locationProvider.getCurrentLocationFlow(5000).collect() {
                    _currentLocation.value = it
                }
            }
        }
    }

    private fun getGpsStatus() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                (locationProvider as LocationProviderImpl).isGPSEnabled().collect() {
                    _isGpsEnabled.value = it
                }
            }
        }
    }

    private fun loadLastLocation() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val (latitude, longitude) = sharedPreferencesRepository.loadLastLocation()
                val location = Location("")
                location.latitude = latitude
                location.longitude = longitude
                _lastLocation.update { location }
            }
        }
    }

    private fun saveLastLocation() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                if (currentLocation.value.latitude != 0.0 && currentLocation.value.longitude != 0.0) {
                    sharedPreferencesRepository.saveLastLocation(
                        currentLocation.value.latitude,
                        currentLocation.value.longitude
                    )
                }
            }
        }
    }

    private fun getAllGeoNotes() = mapRepository.getAllGeoNotesFromLocal()
        .map { geoNotesList -> geoNotesList.map { GeoNoteDisplayable(it) } }

    fun saveGeoNoteToLocal(geoNote: GeoNoteDisplayable) {
        viewModelScope.launch { mapRepository.saveGeoNoteToLocal(geoNote.toGeoNoteDomain()) }
    }

    fun update(geoNote: GeoNoteDisplayable) {
        viewModelScope.launch { mapRepository.updateGeoNote(geoNote.toGeoNoteDomain()) }
    }

    fun updateLocationToZoom(geoPoint: GeoPoint?) {
        _locationToZoomLocation.update { geoPoint }
    }

    fun updateClickedLocation(geoPoint: GeoPoint?) {
        _clickedLocation.update { geoPoint }
    }

    private fun dropDataBase() {
        viewModelScope.launch { mapRepository.dropDataBase() }
    }

    fun deleteGeoNote(id: UUID) {
        viewModelScope.launch { withContext(Dispatchers.IO) { mapRepository.deleteGeoNote(id) } }
    }

    fun getLastLocation() = currentLocation.value

    override fun onCleared() {
        super.onCleared()
        saveLastLocation()
        Log.d("VIEWMODEL", "CLEARED")
    }
}