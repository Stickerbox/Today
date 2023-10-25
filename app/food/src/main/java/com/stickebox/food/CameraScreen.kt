package com.stickebox.food

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import com.stickebox.common.rotate
import kotlinx.coroutines.launch

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
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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
