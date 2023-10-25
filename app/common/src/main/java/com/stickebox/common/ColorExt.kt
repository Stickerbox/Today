package com.stickebox.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.random.Random

/**
 * Whether the current color is considered dark, and thus can be used to inform whether it'd be
 * visually and for ayy1 purposes to have the opposite luminance text and icons on top of
 *
 * @return Whether the current [Color] is considered dark
 */
fun Color.isDark(): Boolean = luminance() < 0.5

fun Color.lightOrDark(): Color {
    return if (this.isDark()) Color.White else Color.DarkGray
}

/**
 * Generates a random color using Kotlin's [Random.Default] class
 *
 * @return The randomly generated [Color]
 */
fun generateRandomColor(): Color {
    return Color(
        red = Random.Default.nextDouble(from = 0.0, until = 1.0).toFloat(),
        blue = Random.Default.nextDouble(from = 0.0, until = 1.0).toFloat(),
        green = Random.Default.nextDouble(from = 0.0, until = 1.0).toFloat()
    )
}

/**
 * Takes a [Color] and generates a complimentary [Color].
 * This is usually done by taking the rgb values and doing 255 (the max value) minus the r, g, or b
 * value.
 * In this case, the rgb values for [Color] range from 0.0 to 1.0, so take 1.0 as 255.
 * The alpha value will be copied from the passed in [color] value.
 *
 * @param color The [Color] for which this function will generate an opposite complimentary color
 *
 * @return The complimentary [Color]
 */
fun generateComplimentaryColor(color: Color): Color {
    val complimentaryRed = 1.0f - color.red
    val complimentaryBlue = 1.0f - color.blue
    val complimentaryGreen = 1.0f - color.green
    return Color(
        red = complimentaryRed,
        blue = complimentaryBlue,
        green = complimentaryGreen,
        alpha = color.alpha
    )
}
