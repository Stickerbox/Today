package com.stickebox.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.io.ByteArrayOutputStream
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class PersistedFoodItem : Persistable, RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var timeAdded: RealmInstant = RealmInstant.now()
    var description: String = ""
    var image: ByteArray? = null
}

fun PersistedFoodItem.toDomainModel(formatter: DateTimeFormatter): FoodItem {
    val timeAddedLocalDateTime = this.timeAdded.toLocalDateTime()
    return FoodItem(
        timeAdded = timeAddedLocalDateTime.format(formatter).lowercase(),
        description = this.description,
        image = this@toDomainModel.image?.toBitmap()?.asImageBitmap() ?: randomBitmapColor().asImageBitmap(),
        timeAddedLocalDateTime = timeAddedLocalDateTime
    )
}

fun FoodItem.toPersistedModel(): PersistedFoodItem {
    return PersistedFoodItem().apply {
        timeAdded = RealmInstant.from(
            timeAddedLocalDateTime.toEpochSecond(ZoneOffset.UTC), timeAddedLocalDateTime.nano
        )
        description = this@toPersistedModel.description
        image = this@toPersistedModel.image.asAndroidBitmap().toByteArray()
    }
}

private fun randomBitmapColor(): Bitmap {
    val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(generateRandomColor().toArgb())
    return bitmap
}
