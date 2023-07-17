package com.example.szacunki.features.pdf.viewer

import android.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.example.szacunki.core.calculations.color2
import com.ramcosta.composedestinations.annotation.Destination
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import java.io.File

@Destination
@Composable
fun PdfViewerScreen(path: String) {
    val file = File(path)
    var uri = Uri.fromFile(file)
    val context = LocalContext.current
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Local(uri),
        isZoomEnable = true
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        VerticalPDFReader(
            state = pdfState,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
        )
        pdfState.file?.let {
            FloatingActionButton(
                onClick = {
                    uri = FileProvider.getUriForFile(
                        context,
                        "com.example.szacunki.filecprovider",
                        file
                    )
                    share(uri, context)
                },
                backgroundColor = color2,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu_share),
                    contentDescription = null
                )
            }
        }
    }
}


fun share(uri: Uri, context: Context) {

    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "application/pdf"
    intent.putExtra(Intent.EXTRA_STREAM, uri)

    startActivity(context, intent, null)
}