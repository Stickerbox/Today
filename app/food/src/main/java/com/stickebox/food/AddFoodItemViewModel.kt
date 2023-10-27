package com.stickebox.food

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.common.FoodItem
import com.stickebox.common.Repository
import com.stickebox.common.generateRandomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime

@KoinViewModel
class AddFoodItemViewModel(
    private val repository: Repository
) : ViewModel() {

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
        _image.update {
            FoodItemPicture(isColor = true, bitmap = bitmap.asImageBitmap())
        }

        // Enable save if either the image is captured from camera, or the description has text
        viewModelScope.launch {
            merge(description.map { it.isEmpty() }, image.map { it?.isColor ?: false })
                .collectLatest {
                    _canSave.value = !it
                }
        }
    }

    fun imageCaptured(bitmap: Bitmap) {
        _image.update {
            FoodItemPicture(isColor = false, bitmap = bitmap.asImageBitmap())
        }
    }

    fun onComplete() {
        // Save to database
        viewModelScope.launch {
            repository.saveFoodItem(
                FoodItem(
                    timeAdded = "Do not care about this value when saving",
                    description = description.value,
                    image = _image.value!!.bitmap,
                    timeAddedLocalDateTime = LocalDateTime.now()
                )
            )
        }
    }
}
