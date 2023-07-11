package com.example.szacunki.features.estimation.presentation.screens.estimation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.szacunki.R
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.extensions.toLocalDate
import com.example.szacunki.core.extensions.trimToDisplaySectionNumber
import com.example.szacunki.destinations.PdfViewerScreenDestination
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.pdf.creator.PdfGenerator
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun BottomInfoBar(
    estimation: State<EstimationDisplayable>,
    memoState: MutableState<Boolean>,
    navigator: DestinationsNavigator,
    context: Context,
    treeIndexState: MutableState<Int>,
    listState: LazyListState,
    scope: CoroutineScope
) {


    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        backgroundColor = color2
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = color2), contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_pdf),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .height(40.dp)
                        .width(40.dp)
                        .background(color2)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    val path = PdfGenerator.generatePdf(context, estimation.value)
                                    val navArg = PdfViewerScreenDestination.NavArgs(path = path)
                                    navigator.navigate(PdfViewerScreenDestination(navArg))
                                },
                                onTap = {
                                    Toast
                                        .makeText(
                                            context,
                                            "Przytrzymaj",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                })
                        }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75F)
                        .clip(RoundedCornerShape(100))
                        .padding(top = 10.dp, bottom = 10.dp)
                        .background(MaterialTheme.colors.background)

                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)

                    ) {
                        Text(
                            text = "Oddz. nr.: ${estimation.value.sectionNumber.trimToDisplaySectionNumber()}",
                            style = MaterialTheme.typography.subtitle1,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Data: ${estimation.value.date.toLocalDate()}",
                            style = MaterialTheme.typography.subtitle1,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.5F)
                                .fillMaxSize()
                                .clickable {
                                    if (treeIndexState.value != 0) {
                                        scope.launch {
                                            treeIndexState.value--
                                            if (treeIndexState.value != 0) {
                                                listState.scrollToItem(treeIndexState.value - 1)
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(start = 5.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(0.5F)
                                .fillMaxSize()
                                .clickable {
                                    if (treeIndexState.value != estimation.value.trees.size - 1) {
                                        treeIndexState.value++
                                        scope.launch { listState.scrollToItem(treeIndexState.value - 1) }
                                    }
                                },
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 5.dp)
                            )
                        }
                    }
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_note),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .height(40.dp)
                        .width(40.dp)
                        .background(color2)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    memoState.value = true
                                },
                                onTap = {
                                    Toast
                                        .makeText(
                                            context,
                                            "Przytrzymaj",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                })
                        }
                )
            }
        }
    }
}



