package com.example.szacunki.features.estimation.presentation.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.szacunki.core.calculations.color1
import com.example.szacunki.core.calculations.color2
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.screens.destinations.EstimationScreenDestination
import com.example.szacunki.features.pdf.creator.PdfGenerator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import java.util.*

@Destination
@Composable
fun SavedEstimationsScreen(navigator: DestinationsNavigator) {
    val viewModel = getViewModel<SavedEstimationsViewModel>()
    val estimations = viewModel.estimations.collectAsState(emptyList())

    Scaffold(
        topBar = { SavedEstimationsTopBar() },
        bottomBar = { SavedEstimationsBottomBar(viewModel = viewModel) },
        content = {
            AllSavedEstimationsContent(estimations = estimations, navigator = navigator)
            it
        })
}

@Composable
fun SavedEstimationsTopBar() {
    TopAppBar(modifier = Modifier.fillMaxWidth(), backgroundColor = color2) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Zapisane Szacunki Brakarskie",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5
            )
        }

    }
}

@Composable
fun AllSavedEstimationsContent(
    estimations: State<List<EstimationDisplayable>>,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    LazyColumn(
        reverseLayout = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp)
    ) {
        items(estimations.value) {
            Row() {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            val id = it.id
                            val navArg = EstimationScreenDestination.NavArgs(
                                id = id,
                                sectionNumber = null,
                                treeNames = null
                            )
                            navigator.navigate(EstimationScreenDestination(navArg))

                        }, elevation = 10.dp, shape = RoundedCornerShape(10.dp)
                ) {
                    EstimationView(name = it.sectionNumber, date = it.date)

                }
                OutlinedButton(onClick = { PdfGenerator.generatePdf(context, it) }) {
                    Text(text = "PDF")
                }
            }
        }

    }
}

@Composable
fun EstimationView(name: String, date: Date) {
    Column(
        modifier = Modifier
            .background(color1)
    ) {
        val title = "Oddział nr.: $name" + "XXX"
        Text(
            text = title,
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h5
        )
        Text(
            text = date.time.toString(),
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h6
        )
    }

}

@Composable
fun SavedEstimationsBottomBar(viewModel: SavedEstimationsViewModel) {
    BottomAppBar(modifier = Modifier.fillMaxWidth(), backgroundColor = color2) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { viewModel.dropDataBase() }) {
                Text(text = "Drop DataBase")
            }
        }
    }
}

