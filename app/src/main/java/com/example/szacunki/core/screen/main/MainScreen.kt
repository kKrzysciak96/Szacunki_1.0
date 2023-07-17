package com.example.szacunki.core.screen.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.szacunki.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    DestinationsNavHost(navController = navController, navGraph = NavGraphs.root)

}
