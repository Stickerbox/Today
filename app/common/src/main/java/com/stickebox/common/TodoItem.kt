package com.stickebox.common

import java.time.LocalDateTime

/**
 * @param time The formatted time of this item's start and end point
 * @param text The content of what this item wants the user to do
 */
data class TodoItem(
    val time: String,
    val text: String,
    val isCurrentItem: Boolean,
    val fromTime: LocalDateTime,
    val toTime: LocalDateTime,
)
