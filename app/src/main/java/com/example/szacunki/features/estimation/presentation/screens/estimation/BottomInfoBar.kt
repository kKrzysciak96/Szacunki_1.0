package com.example.szacunki.features.estimation.presentation.screens.estimation

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.szacunki.R
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.core.extensions.toLocalDate
import com.example.szacunki.features.destinations.PdfViewerScreenDestination
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.pdf.creator.PdfGenerator
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomInfoBar(
    estimation: State<EstimationDisplayable>,
    memoState: MutableState<Boolean>,
    navigator: DestinationsNavigator,
    context: Context
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
                        .clickable {

                            val path = PdfGenerator.generatePdf(context, estimation.value)
                            val navArg = PdfViewerScreenDestination.NavArgs(path = path)
                            navigator.navigate(PdfViewerScreenDestination(navArg))
                        }
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Oddział nr.: ${estimation.value.sectionNumber}",
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 26.sp
                    )
                    Text(
                        text = "Data: ${estimation.value.date.toLocalDate()}",
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_note),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .height(40.dp)
                        .width(40.dp)
                        .background(color2)
                        .clickable { memoState.value = true }
                )

            }
        }
    }
}



