package com.stickebox.food

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PersistedFoodItem(val timeAdded: LocalDateTime, val description: String)

fun PersistedFoodItem.toDomainModel(formatter: DateTimeFormatter) =
    FoodItem(
        timeAdded = this.timeAdded.format(formatter).lowercase(),
        description = this.description
    )

val TEST_PERSISTED_FOOD_ITEMS = listOf(
    PersistedFoodItem(
        timeAdded = LocalDateTime.of(2023, 10, 24, 10, 2, 0),
        description = "A single piece of egg"
    ),
    PersistedFoodItem(
        timeAdded = LocalDateTime.of(2023, 10, 24, 13, 48, 0),
        description = "Strawberry smoothie with 100 bananas"
    ),
    PersistedFoodItem(
        timeAdded = LocalDateTime.of(2023, 10, 24, 14, 32, 0),
        description = "Banana smoothie with egg"
    ),
    PersistedFoodItem(
        timeAdded = LocalDateTime.of(2023, 10, 24, 17, 10, 0),
        description = "Egg smoothie with sardine sandwich"
    ),
    PersistedFoodItem(
        timeAdded = LocalDateTime.of(2023, 10, 24, 0, 17, 0),
        description = "Dog"
    )
)