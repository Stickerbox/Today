package com.stickebox.food

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.common.generateRandomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class AddFoodItemViewModel : ViewModel() {

    private var _image: MutableStateFlow<FoodItemPicture?> = MutableStateFlow(null)
    val image: StateFlow<FoodItemPicture?>
        get() = _image.asStateFlow()

    private var _canSave: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val canSave: StateFlow<Boolean>
        get() = _canSave.asStateFlow()

    var description = MutableStateFlow("")

    init {
        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(generateRandomColor().toArgb())
        viewModelScope.launch {
            _image.emit(FoodItemPicture(isColor = true, bitmap = bitmap.asImageBitmap()))
        }

        // Enable save if either the image is captured from camera, or the description has text
        viewModelScope.launch {
            merge(description.map { it.isEmpty() }, image.map { it?.isColor ?: false })
                .collectLatest {
                    _canSave.value = !it
                }
        }
    }

    suspend fun imageCaptured(bitmap: Bitmap) {
        _image.emit(FoodItemPicture(isColor = false, bitmap = bitmap.asImageBitmap()))
    }

    fun onComplete() {
        // Save to database
    }
}
