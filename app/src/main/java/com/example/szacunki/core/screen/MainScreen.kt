package com.example.szacunki.core.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.szacunki.features.estimation.presentation.screens.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    DestinationsNavHost(navController = navController, navGraph = NavGraphs.root)

}
