package com.stickebox.common

import java.time.LocalDateTime

/**
 * @param fromTime The time that this item starts
 * @param toTime The time that this item ends
 * @param text The content of what this item wants the user to do
 */
data class PersistedTodoItem(
    val fromTime: LocalDateTime,
    val toTime: LocalDateTime,
    val text: String,
)
