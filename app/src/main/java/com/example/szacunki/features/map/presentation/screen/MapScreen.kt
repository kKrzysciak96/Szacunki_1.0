package com.example.szacunki.features.map.presentation.screen

import android.util.Log
import android.widget.Toast
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
import com.example.szacunki.R
import com.example.szacunki.core.extensions.*
import com.example.szacunki.features.map.presentation.model.GeoNoteDisplayable
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel
import org.osmdroid.events.MapListener
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.util.*


@Destination(route = "MapScreen")
@Composable
fun MapScreen() {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            CustomMapView(mapView = mapView)
        }
    }
}


@Composable
fun CustomMapView(mapView: MapView) {

    val viewModel = getViewModel<MapViewModel>()
    val isGpsEnabled = viewModel.isGpsEnabled.collectAsState()
    val locationToZoom = viewModel.locationToZoomLocation.collectAsState()
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
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (isObjectToUpdateDialogVisible.value || isObjectOnMapClickedDialogVisible.value) {
            ObjectDialog(geoNoteToUpdate = if (chosenInfoWindowId.value != null) geoNotes.value.first { it.id == chosenInfoWindowId.value } else null,
                latitude = if (isObjectToUpdateDialogVisible.value) {
                    currentLocation.value.latitude
                } else {
                    locationToZoom.value?.latitude ?: 0.0
                },
                longitude = if (isObjectToUpdateDialogVisible.value) {
                    currentLocation.value.longitude
                } else {
                    locationToZoom.value?.longitude ?: 0.0
                },
                viewModel = viewModel,
                onDismiss = { latitude, longitude ->
                    chosenInfoWindowId.value = null
                    isObjectToUpdateDialogVisible.value = false
                    isObjectOnMapClickedDialogVisible.value = false
                    isDeleteMarkerDialogVisible.value = false
                    viewModel.updateLocationToZoom(GeoPoint(latitude, longitude))

                })
        }
        if (!isObjectToUpdateDialogVisible.value && !isObjectOnMapClickedDialogVisible.value && isDeleteMarkerDialogVisible.value) {
            MarkerDeleteDialog(
                mapView = mapView,
                id = chosenMarkerId.value,
                viewModel = viewModel,
                onDismiss = {
                    isDeleteMarkerDialogVisible.value = false
                })
        }

        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = {
                it.apply {
                    Log.d("ANDROIDVIEW", "UPDATE")
                }
            }
        )
        LaunchedEffect(key1 = locationToZoom.value, block = {
            mapView.goToPosition(locationToZoom.value, cameraState.value.zoom)
        })

        if (currentLocation.value.latitude != 0.0 && currentLocation.value.latitude != 0.0 && isGpsEnabled.value) {
            Card(
                border = BorderStroke(width = 2.dp, color = Color.Black),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 5.dp)
            )
            {
                Text(
                    text = "${currentLocation.value.latitude} ${currentLocation.value.longitude}",
                    color = Color.Black,
                    modifier = Modifier.padding(5.dp)
                )
            }
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_refresh),
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = angle
                    }
                    .align(Alignment.TopCenter)
                    .size(50.dp)
            )
        }
        IconButton(
            onClick = {
                mapView.refreshLocation()

                if (currentLocation.value.latitude == 0.0 && currentLocation.value.longitude == 0.0) {
                    mapView.goToPosition(locationToZoom.value)
                } else {
                    mapView.goToPosition(currentLocation.value)
                }

            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, top = 20.dp, end = 20.dp, start = 20.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_my_location),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }
        IconButton(
            onClick = {
                if (isGpsEnabled.value) {
                    isObjectToUpdateDialogVisible.value = true
                } else {
                    Toast.makeText(mapView.context, "Włącz Lokalizację", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, top = 20.dp, end = 20.dp, start = 20.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_add_location),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = if (isGpsEnabled.value) Color.Black else Color.Red
            )
        }

    }

    LaunchedEffect(key1 = geoNotes.value, block = {
        Log.d("REDRAW", "REDRAW")
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


    LaunchedEffect(key1 = Unit, block = {
        mapView.apply {
            setMapConfigurations()
            setTileSource(TileSourceFactory.MAPNIK)
            addPinFeature(longPressHelperListener = {
                viewModel.updateLocationToZoom(it)
                isObjectOnMapClickedDialogVisible.value = true
            })
            val listener: MapListener = CustomMapListener {
                viewModel.updateCameraState(
                    mapCenter.latitude,
                    mapCenter.longitude,
                    zoomLevelDouble
                )
            }
            addMapListener(listener)
        }
    })
}

fun redrawMapView(
    viewModel: MapViewModel,
    geoNotes: State<List<GeoNoteDisplayable>>,
    mapView: MapView,
    chosenMarkerId: MutableState<UUID?>,
    chosenInfoWindowId: MutableState<UUID?>,
    isDeleteMarkerDialogVisible: MutableState<Boolean>,
    isObjectDialogVisible: MutableState<Boolean>
) {
    geoNotes.value.forEach { geoNote ->
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
            }, onLongInfoWindowListener = {
                chosenInfoWindowId.value = it
                isObjectDialogVisible.value = true
            })
        }


    }
}
