package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddMultipleTreeDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier
                .background(Color.Red)
                .size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Dialog")
        }
    }
}