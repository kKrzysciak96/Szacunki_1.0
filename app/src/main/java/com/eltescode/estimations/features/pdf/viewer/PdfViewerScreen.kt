package com.eltescode.estimations.features.pdf.viewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.eltescode.estimations.core.extensions.shareFile
import com.eltescode.estimations.ui.theme.color2
import com.eltescode.estimations.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.math.sqrt


@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun PdfViewerScreen(
    path: String,
) {
    val file = rememberSaveable { File(path) }
    val uri = rememberSaveable { Uri.fromFile(file) }
    val rendererScope = rememberCoroutineScope()
    val scrollScope = rememberCoroutineScope()
    val mutex = remember { Mutex() }
    val context = LocalContext.current
    val imageLoader = LocalContext.current.imageLoader
    val imageLoadingScope = rememberCoroutineScope()
    val scale = remember { mutableStateOf(1f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val pagerState = rememberPagerState(0)
    val renderer by produceState<PdfRenderer?>(null, uri) {
        rendererScope.launch(Dispatchers.IO) {
            val input = ParcelFileDescriptor.open(uri.toFile(), ParcelFileDescriptor.MODE_READ_ONLY)
            value = PdfRenderer(input)
        }
        awaitDispose {
            val currentRenderer = value
            rendererScope.launch(Dispatchers.IO) {
                mutex.withLock {
                    currentRenderer?.close()
                }
            }
        }
    }
    val state = rememberTransformableState { scaleChange, offsetChange, _ ->
        scale.value = adjustScale(scale.value, scaleChange)
        offset.value += adjustOffset(scale.value, offsetChange)
    }
    val pageCount by remember(renderer) { derivedStateOf { renderer?.pageCount ?: 0 } }

    PdfViewerScreen(
        file = file,
        uri = uri,
        scrollScope = scrollScope,
        imageLoadingScope = imageLoadingScope,
        mutex = mutex,
        context = context,
        imageLoader = imageLoader,
        renderer = renderer,
        pageCount = pageCount,
        pagerState = pagerState,
        state = state,
        scale = scale,
        offset = offset
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PdfViewerScreen(
    file: File,
    uri: Uri,
    scrollScope: CoroutineScope,
    imageLoadingScope: CoroutineScope,
    mutex: Mutex,
    context: Context,
    imageLoader: ImageLoader,
    renderer: PdfRenderer?,
    pageCount: Int,
    pagerState: PagerState,
    state: TransformableState,
    scale: MutableState<Float>,
    offset: MutableState<Offset>,
) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .transformable(state = state),
    ) {
        val width = with(LocalDensity.current) { maxWidth.toPx() }.toInt()
        val height = (width * sqrt(2f)).toInt()
        HorizontalPager(pageCount = pageCount,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    translationX = offset.value.x,
                    translationY = offset.value.y
                ),
            state = pagerState,
            userScrollEnabled = false,
            key = { index -> "$uri-$index" }) { index ->
            val cacheKey = MemoryCache.Key("$uri-$index")
            var bitmap by remember { mutableStateOf(imageLoader.memoryCache?.get(cacheKey) as? Bitmap?) }
            if (bitmap == null) {
                DisposableEffect(uri, index) {
                    val job = imageLoadingScope.launch(Dispatchers.IO) {
                        val destinationBitmap =
                            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        mutex.withLock {
                            if (!coroutineContext.isActive) return@launch
                            try {
                                renderer?.let {
                                    it.openPage(index).use { page ->
                                        page.render(
                                            destinationBitmap,
                                            null,
                                            null,
                                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                //Just catch and return in case the renderer is being closed
                                return@launch
                            }
                        }
                        bitmap = destinationBitmap
                    }
                    onDispose {
                        job.cancel()
                    }
                }
            } else {
                val request = ImageRequest
                    .Builder(context)
                    .size(width, height)
                    .data(bitmap)
                    .build()
                Image(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize(),
                    painter = rememberAsyncImagePainter(request),
                    contentDescription = null
                )
            }
        }
        PreviousPageButton(
            modifier = Modifier.align(Alignment.BottomStart),
            scrollScope = scrollScope,
            scale = scale,
            offset = offset,
            pagerState = pagerState
        )
        CurrentPageButton(
            pageCount = pageCount,
            modifier = Modifier.align(Alignment.BottomCenter),
            scale = scale,
            offset = offset,
            pagerState = pagerState
        )

        NextPageButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            scrollScope = scrollScope,
            scale = scale,
            offset = offset,
            pagerState = pagerState
        )
        ShareButton(
            file = file,
            context = context,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreviousPageButton(
    scrollScope: CoroutineScope,
    scale: MutableState<Float>,
    offset: MutableState<Offset>,
    pagerState: PagerState,
    modifier: Modifier,
) {
    OutlinedButton(modifier = modifier
        .width(125.dp)
        .padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color2, contentColor = Color.White
        ),
        onClick = {
            scrollScope.launch {
                resetView(scale, offset)
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        }) {
        Text(text = stringResource(id = R.string.hint42))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrentPageButton(
    pageCount: Int,
    scale: MutableState<Float>,
    offset: MutableState<Offset>,
    pagerState: PagerState,
    modifier: Modifier,
) {
    OutlinedButton(modifier = modifier.padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent, contentColor = Color.Black
        ),
        onClick = {
            resetView(scale, offset)
        }) {
        Text(text = "${pagerState.currentPage + 1} z $pageCount")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NextPageButton(
    scrollScope: CoroutineScope,
    scale: MutableState<Float>,
    offset: MutableState<Offset>,
    pagerState: PagerState,
    modifier: Modifier,
) {
    OutlinedButton(modifier = modifier
        .width(125.dp)
        .padding(10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color2, contentColor = Color.White
        ),
        onClick = {
            resetView(scale, offset)
            scrollScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
        }) {
        Text(text = stringResource(id = R.string.hint43))
    }
}

@Composable
fun ShareButton(file: File, context: Context, modifier: Modifier) {
    FloatingActionButton(
        onClick = {
            val uri = FileProvider.getUriForFile(
                context, "com.eltescode.estimations.fileprovider", file
            )
            context.shareFile(uri)
        }, backgroundColor = color2, modifier = modifier.padding(end = 20.dp, bottom = 80.dp)
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_share),
            contentDescription = null
        )
    }
}

private fun adjustScale(scale: Float, scaleChange: Float): Float {
    return if (scale < 0.5F) {
        0.5F
    } else {
        scale * scaleChange
    }
}

private fun adjustOffset(scale: Float, offsetChange: Offset) = when (scale) {
    in 0.0F..1F -> offsetChange
    in 1F..1.25F -> offsetChange * 1.5F
    in 1.25F..1.75F -> offsetChange * 2F
    in 1.75F..2.5F -> offsetChange * 2.5F
    else -> offsetChange * 3F
}

private fun resetView(scale: MutableState<Float>, offset: MutableState<Offset>) {
    scale.value = 1F
    offset.value = Offset.Zero
}