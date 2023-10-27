package com.stickebox.food

import androidx.lifecycle.ViewModel
import com.stickebox.common.FoodItem
import com.stickebox.common.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@KoinViewModel
class FoodScreenViewModel(
    repository: Repository
) : ViewModel() {
    private val headerFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
    private val now: LocalDateTime = LocalDateTime.now()
    val currentDate: String = now.format(headerFormatter)

    val foodItems: Flow<Map<Int, List<FoodItem>>> = repository.getFoodItems(now)
        .map { foodItems ->
            val map = hashMapOf<Int, MutableList<FoodItem>>()

            foodItems.forEach { foodItem ->
                if (foodItem.timeAddedLocalDateTime.hour in 6..12) {
                    val list = map[0] ?: mutableListOf()
                    list.add(foodItem)
                    map[0] = list
                }

                if (foodItem.timeAddedLocalDateTime.hour in 13..16) {
                    val list = map[1] ?: mutableListOf()
                    list.add(foodItem)
                    map[1] = list
                }

                if (foodItem.timeAddedLocalDateTime.hour in 17..24) {
                    val list = map[2] ?: mutableListOf()
                    list.add(foodItem)
                    map[2] = list
                }

                if (foodItem.timeAddedLocalDateTime.hour in 0..6) {
                    val list = map[2] ?: mutableListOf()
                    list.add(foodItem)
                    map[2] = list
                }
            }

            return@map map
        }
}
