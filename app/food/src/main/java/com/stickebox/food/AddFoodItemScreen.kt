package com.stickebox.food

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.compose.BackHandler
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.stickebox.common.generateRandomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FoodItemPicture(val isColor: Boolean, val bitmap: ImageBitmap)

class AddFoodItemViewModel : ViewModel() {

    private var _image: MutableStateFlow<FoodItemPicture?> = MutableStateFlow(null)
    val image: StateFlow<FoodItemPicture?>
        get() = _image.asStateFlow()

    var description = MutableStateFlow("")

    init {
        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(generateRandomColor().toArgb())
        viewModelScope.launch {
            _image.emit(FoodItemPicture(isColor = true, bitmap = bitmap.asImageBitmap()))
        }
    }

    suspend fun imageCaptured(bitmap: Bitmap) {
        _image.emit(FoodItemPicture(isColor = false, bitmap = bitmap.asImageBitmap()))
    }
}

typealias LegacyColor = android.graphics.Color

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddFoodItemScreen(
    modifier: Modifier = Modifier,
    viewModel: AddFoodItemViewModel
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    var showCamera by remember { mutableStateOf(false) }
    val image by viewModel.image.collectAsState()
    val description by viewModel.description.collectAsState()
    var dominantColor by remember { mutableStateOf(Color.DarkGray) }

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .padding(bottom = 100.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .shadow(30.dp, shape = RoundedCornerShape(40.dp), ambientColor = dominantColor, spotColor = dominantColor)
                    .clickable {
                        showCamera = true
                    }
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.DarkGray)
                    .size(250.dp)
            ) {
                image?.let {
                    Palette.Builder(it.bitmap.asAndroidBitmap()).generate { palette ->
                        dominantColor = Color(palette?.getVibrantColor(0x000000) ?: Color.DarkGray.toArgb())
                    }
                    Image(
                        bitmap = it.bitmap,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )

                    if (it.isColor) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "Take photo",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 24.dp))
            TextField(
                value = description,
                onValueChange = { viewModel.description.value = it }
            )
        }

        AnimatedVisibility(
            visible = showCamera,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (cameraPermissionState.status.isGranted) {
                CameraScreen(
                    viewModel = viewModel,
                    modifier = modifier,
                    onComplete = { showCamera = false })
            } else {
                NoPermissionScreen {

                }
            }
        }

        BackHandler(enabled = showCamera) {
            showCamera = false
        }
    }
}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: AddFoodItemViewModel,
    onComplete: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    val coroutineScope = rememberCoroutineScope()

    Box {

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setBackgroundColor(LegacyColor.WHITE)
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also { previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color.Black)
                .align(Alignment.BottomCenter)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Black)
                .align(Alignment.TopCenter)
        )

        FloatingActionButton(
            containerColor = Color.White,
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            content = {
                Icon(Icons.Rounded.ThumbUp, contentDescription = "Take photo")
            },
            onClick = {
                val mainExecutor = ContextCompat.getMainExecutor(context)
                cameraController.takePicture(
                    mainExecutor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            super.onCaptureSuccess(image)

                            coroutineScope.launch {
                                val imageBitmap = image.toBitmap().scale(500, 500)
                                viewModel.imageCaptured(imageBitmap.rotate(90f))
                                image.close()
                                onComplete()
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            super.onError(exception)
                            onComplete()
                        }
                    })
            }
        )
    }
}

@Composable
fun NoPermissionScreen(
    onRequestPermission: () -> Unit
) {
    Box {
        Text(
            text = "plz give me permission",
            modifier = Modifier.clickable { onRequestPermission() })
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}