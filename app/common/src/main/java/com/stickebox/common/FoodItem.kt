package com.stickebox.common

import androidx.compose.ui.graphics.ImageBitmap
import java.time.LocalDateTime

data class FoodItem(
    val timeAdded: String,
    val description: String,
    val image: ImageBitmap,
    val timeAddedLocalDateTime: LocalDateTime
)
