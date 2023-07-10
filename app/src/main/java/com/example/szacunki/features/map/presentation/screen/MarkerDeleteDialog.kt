package com.example.szacunki.features.map.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.extensions.CustomMarker
import org.osmdroid.views.MapView
import java.util.*


@Composable
fun MarkerDeleteDialog(
    mapView: MapView,
    id: UUID?,
    viewModel: MapViewModel,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Card() {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Czy napewno usunąć marker?",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(modifier = Modifier.padding(20.dp)) {
                        OutlinedButton(
                            modifier = Modifier.padding(10.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                            border = BorderStroke(width = 1.dp, color = Color.Black),
                            onClick = {
                                id?.let { id ->
                                    (mapView.overlays.find { predicate ->
                                        (predicate is CustomMarker && predicate.id == id.toString())

                                    }).let {
                                        (it as CustomMarker).closeInfoWindow()
                                        mapView.overlays.remove(it)
                                    }
                                    viewModel.deleteGeoNote(id = id)
                                }
                                onDismiss()
                            })
                        {
                            Text(text = "USUŃ", style = MaterialTheme.typography.h5)
                        }
                        OutlinedButton(
                            modifier = Modifier.padding(10.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = color2),
                            border = BorderStroke(width = 1.dp, color = Color.DarkGray),
                            onClick = { onDismiss() })
                        {
                            Text(text = "WRÓĆ", style = MaterialTheme.typography.h5)
                        }

                    }
                }
            }

        }

    }
}