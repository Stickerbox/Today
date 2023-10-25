package com.stickebox.food

import android.Manifest
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

typealias LegacyColor = android.graphics.Color
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddFoodItemScreen(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit,
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 100.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        30.dp,
                        shape = RoundedCornerShape(40.dp),
                        ambientColor = dominantColor,
                        spotColor = dominantColor
                    )
                    .clickable {
                        showCamera = true
                    }
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.DarkGray)
                    .size(250.dp)
            ) {
                image?.let {
                    Palette.Builder(it.bitmap.asAndroidBitmap()).generate { palette ->
                        dominantColor =
                            Color(palette?.getVibrantColor(0x000000) ?: Color.DarkGray.toArgb())
                    }
                    Image(
                        bitmap = it.bitmap,
                        contentDescription = "Photo",
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
                onValueChange = { viewModel.description.value = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.onComplete()
                        onComplete()
                    })
            )

            val canSave by viewModel.canSave.collectAsState()

            Button(
                onClick = {
                    viewModel.onComplete()
                    onComplete()
                },
                enabled = canSave,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Save")
            }
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
            }
        }

        BackHandler(enabled = showCamera) {
            showCamera = false
        }
    }
}
