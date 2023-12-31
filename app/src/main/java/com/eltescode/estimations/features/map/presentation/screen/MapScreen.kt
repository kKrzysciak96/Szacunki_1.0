package com.eltescode.estimations.features.map.presentation.screen

import android.location.Location
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.eltescode.estimations.core.extensions.*
import com.eltescode.estimations.features.map.presentation.custom.CameraState
import com.eltescode.estimations.features.map.presentation.custom.CustomMapListener
import com.eltescode.estimations.features.map.presentation.model.GeoNoteDisplayable
import com.eltescode.estimations.R
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.util.*


@Destination
@Composable
fun MapScreen(viewModel: MapViewModel = koinViewModel()) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val isGpsEnabled = viewModel.isGpsEnabled.collectAsState()
    val locationToZoom = viewModel.locationToZoom.collectAsState()
    val currentLocation = viewModel.currentLocation.collectAsState()
    val cameraState = viewModel.cameraState.collectAsState()
    val geoNotes = viewModel.geoNotes.collectAsState()
    val isObjectToUpdateDialogVisible = rememberSaveable { mutableStateOf(false) }
    val isObjectOnMapClickedDialogVisible = rememberSaveable { mutableStateOf(false) }
    val isDeleteMarkerDialogVisible = rememberSaveable { mutableStateOf(false) }
    val chosenMarkerId = rememberSaveable { mutableStateOf<UUID?>(null) }
    val chosenInfoWindowId = rememberSaveable { mutableStateOf<UUID?>(null) }
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F, targetValue = 360F, animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.onDestroyActivity()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(key1 = Unit, block = {
        mapView.apply {
            setMapConfigurations()
            setTileSource(TileSourceFactory.MAPNIK)
            addPinFeature(longPressHelperListener = { geoPoint ->
                geoPoint?.let {
                    viewModel.updateLocationToZoom(it)
                    isObjectOnMapClickedDialogVisible.value = true
                }
            })
            val listener: MapListener = CustomMapListener {
                viewModel.updateCameraState(
                    mapCenter.latitude, mapCenter.longitude, zoomLevelDouble
                )
            }
            addMapListener(listener)
        }
    })

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            MapScreen(
                viewModel = viewModel,
                mapView = mapView,
                isGpsEnabled = isGpsEnabled.value,
                locationToZoom = locationToZoom.value,
                currentLocation = currentLocation.value,
                cameraState = cameraState.value,
                geoNotes = geoNotes.value,
                isObjectToUpdateDialogVisible = isObjectToUpdateDialogVisible,
                isObjectOnMapClickedDialogVisible = isObjectOnMapClickedDialogVisible,
                isDeleteMarkerDialogVisible = isDeleteMarkerDialogVisible,
                chosenMarkerId = chosenMarkerId,
                chosenInfoWindowId = chosenInfoWindowId,
                angle = angle,
                updateGeoNote = viewModel::updateGeoNote,
                saveGeoNoteToLocal = viewModel::saveGeoNoteToLocal,
                updateLocationToZoom = viewModel::updateLocationToZoom,
                deleteGeoNote = viewModel::deleteGeoNote
            )
        }
    }
}


@Composable
fun MapScreen(
    viewModel: MapViewModel,
    mapView: MapView,
    isGpsEnabled: Boolean,
    locationToZoom: Location,
    currentLocation: Location,
    cameraState: CameraState,
    geoNotes: List<GeoNoteDisplayable>,
    isObjectToUpdateDialogVisible: MutableState<Boolean>,
    isObjectOnMapClickedDialogVisible: MutableState<Boolean>,
    isDeleteMarkerDialogVisible: MutableState<Boolean>,
    chosenMarkerId: MutableState<UUID?>,
    chosenInfoWindowId: MutableState<UUID?>,
    angle: Float,
    updateGeoNote: (GeoNoteDisplayable) -> Unit,
    saveGeoNoteToLocal: (GeoNoteDisplayable) -> Unit,
    updateLocationToZoom: (GeoPoint) -> Unit,
    deleteGeoNote: (UUID) -> Unit

) {
    LaunchedEffect(key1 = geoNotes, block = {
        redrawMapView(
            viewModel = viewModel,
            geoNotes = geoNotes,
            mapView = mapView,
            chosenMarkerId = chosenMarkerId,
            chosenInfoWindowId = chosenInfoWindowId,
            isDeleteMarkerDialogVisible = isDeleteMarkerDialogVisible,
            isObjectDialogVisible = isObjectToUpdateDialogVisible
        )
    })
    LaunchedEffect(key1 = locationToZoom, block = {
        mapView.goToPosition(locationToZoom, cameraState.zoom)
    })
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ShowObjectDialog(
            isObjectToUpdateDialogVisible = isObjectToUpdateDialogVisible,
            isObjectOnMapClickedDialogVisible = isObjectOnMapClickedDialogVisible,
            isDeleteMarkerDialogVisible = isDeleteMarkerDialogVisible,
            chosenInfoWindowId = chosenInfoWindowId,
            geoNotes = geoNotes,
            currentLocation = currentLocation,
            locationToZoom = locationToZoom,
            updateGeoNote = updateGeoNote,
            saveGeoNoteToLocal = saveGeoNoteToLocal,
            updateLocationToZoom = updateLocationToZoom
        )
        ShowMarkerDeleteDialog(
            mapView = mapView,
            isObjectToUpdateDialogVisible = isObjectToUpdateDialogVisible,
            isObjectOnMapClickedDialogVisible = isObjectOnMapClickedDialogVisible,
            isDeleteMarkerDialogVisible = isDeleteMarkerDialogVisible,
            chosenMarkerId = chosenMarkerId,
            deleteGeoNote = deleteGeoNote
        )
        MapView(mapView = mapView)
        LastKnownLocationParameters(
            isGpsEnabled = isGpsEnabled,
            currentLocation = currentLocation,
            angle = angle,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        GoToLastKnownPositionIconButton(
            mapView = mapView,
            locationToZoom = locationToZoom,
            currentLocation = currentLocation,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
        AddObjectIconButton(
            mapView = mapView,
            isGpsEnabled = isGpsEnabled,
            currentLocation = currentLocation,
            isObjectToUpdateDialogVisible = isObjectToUpdateDialogVisible,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

private fun redrawMapView(
    viewModel: MapViewModel,
    geoNotes: List<GeoNoteDisplayable>,
    mapView: MapView,
    chosenMarkerId: MutableState<UUID?>,
    chosenInfoWindowId: MutableState<UUID?>,
    isDeleteMarkerDialogVisible: MutableState<Boolean>,
    isObjectDialogVisible: MutableState<Boolean>
) {
    mapView.invalidate()
    geoNotes.forEach { geoNote ->
        mapView.removeOldMarker(geoNote)
        mapView.apply {
            addMarkerToMap(context = context, geoNote = geoNote, onLongPress = {
                chosenMarkerId.value = it.id
                viewModel.updateLocationToZoom(
                    GeoPoint(
                        geoNote.latitude, geoNote.longitude
                    )
                )
                isDeleteMarkerDialogVisible.value = true
            }, onLongPressInfoWindowListener = {
                chosenInfoWindowId.value = it
                isObjectDialogVisible.value = true
            })
        }
    }
}

@Composable
private fun ShowObjectDialog(
    isObjectToUpdateDialogVisible: MutableState<Boolean>,
    isObjectOnMapClickedDialogVisible: MutableState<Boolean>,
    isDeleteMarkerDialogVisible: MutableState<Boolean>,
    chosenInfoWindowId: MutableState<UUID?>,
    geoNotes: List<GeoNoteDisplayable>,
    currentLocation: Location,
    locationToZoom: Location,
    updateGeoNote: (GeoNoteDisplayable) -> Unit,
    saveGeoNoteToLocal: (GeoNoteDisplayable) -> Unit,
    updateLocationToZoom: (GeoPoint) -> Unit

) {
    if (isObjectToUpdateDialogVisible.value || isObjectOnMapClickedDialogVisible.value) {
        ObjectDialog(geoNoteToUpdate = if (chosenInfoWindowId.value != null) {
            geoNotes.first { it.id == chosenInfoWindowId.value }
        } else {
            null
        },
            latitude = if (isObjectToUpdateDialogVisible.value) {
                currentLocation.latitude
            } else {
                locationToZoom.latitude
            },
            longitude = if (isObjectToUpdateDialogVisible.value) {
                currentLocation.longitude
            } else {
                locationToZoom.longitude
            },
            updateGeoNote = updateGeoNote,
            saveGeoNoteToLocal = saveGeoNoteToLocal,
            updateLocationToZoom = updateLocationToZoom,
            onDismiss = { latitude, longitude ->
                chosenInfoWindowId.value = null
                isObjectToUpdateDialogVisible.value = false
                isObjectOnMapClickedDialogVisible.value = false
                isDeleteMarkerDialogVisible.value = false
                updateLocationToZoom(GeoPoint(latitude, longitude))
            })
    }
}

@Composable
private fun ShowMarkerDeleteDialog(
    mapView: MapView,
    isObjectToUpdateDialogVisible: MutableState<Boolean>,
    isObjectOnMapClickedDialogVisible: MutableState<Boolean>,
    isDeleteMarkerDialogVisible: MutableState<Boolean>,
    chosenMarkerId: MutableState<UUID?>,
    deleteGeoNote: (UUID) -> Unit
) {
    if (!isObjectToUpdateDialogVisible.value && !isObjectOnMapClickedDialogVisible.value && isDeleteMarkerDialogVisible.value) {
        MarkerDeleteDialog(mapView = mapView,
            id = chosenMarkerId.value,
            deleteGeoNote = deleteGeoNote,
            onDismiss = { isDeleteMarkerDialogVisible.value = false })
    }
}

@Composable
private fun MapView(mapView: MapView) {
    AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize(), update = {})
}

@Composable
private fun LastKnownLocationParameters(
    isGpsEnabled: Boolean,
    currentLocation: Location,
    angle: Float,
    modifier: Modifier,
) {
    if (currentLocation.latitude != 0.0 && currentLocation.latitude != 0.0 && isGpsEnabled) {
        Card(
            border = BorderStroke(width = 2.dp, color = Color.Black),
            modifier = modifier.padding(top = 5.dp)
        ) {
            Text(
                text = "${currentLocation.latitude} ${currentLocation.longitude}",
                color = Color.Black,
                modifier = Modifier.padding(5.dp)
            )
        }
    } else {
        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_refresh),
            contentDescription = null,
            modifier = modifier
                .graphicsLayer { rotationZ = angle }
                .size(50.dp))
    }
}

@Composable
private fun GoToLastKnownPositionIconButton(
    mapView: MapView,
    locationToZoom: Location,
    currentLocation: Location,
    modifier: Modifier,
) {
    IconButton(
        onClick = {
            mapView.refreshLocation()
            if (currentLocation.latitude == 0.0 && currentLocation.longitude == 0.0) {
                mapView.goToPosition(locationToZoom)
            } else {
                mapView.goToPosition(currentLocation)
            }
        }, modifier = modifier.padding(bottom = 100.dp, top = 20.dp, end = 20.dp, start = 20.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_my_location),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
private fun AddObjectIconButton(
    mapView: MapView,
    isGpsEnabled: Boolean,
    isObjectToUpdateDialogVisible: MutableState<Boolean>,
    modifier: Modifier,
    currentLocation: Location,
) {
    val isLocationAvailable =
        currentLocation.latitude != 0.0 && currentLocation.latitude != 0.0 && isGpsEnabled
    IconButton(
        onClick = {
            if (isLocationAvailable) {
                isObjectToUpdateDialogVisible.value = true
            } else {
                mapView.context.showLongHint(R.string.hint26)
            }
        }, modifier = modifier.padding(bottom = 20.dp, top = 20.dp, end = 20.dp, start = 20.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_add_location),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = if (isLocationAvailable) Color.Black else Color.Red
        )
    }
}
