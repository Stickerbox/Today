package com.stickebox.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FoodScreenViewModel : ViewModel() {
    private val headerFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
    private val foodItemDateAddedFormatter = DateTimeFormatter.ofPattern("h:mm a")
    val currentDate: String = LocalDateTime.now().format(headerFormatter)

    private val _foodItems: MutableStateFlow<Map<Int, List<FoodItem>>> =
        MutableStateFlow((emptyMap()))
    val foodItems: StateFlow<Map<Int, List<FoodItem>>>
        get() = _foodItems.asStateFlow()

    init {
        val map = hashMapOf<Int, MutableList<FoodItem>>()
        viewModelScope.launch {
            TEST_PERSISTED_FOOD_ITEMS.forEach { persistedFoodItem ->
                if (persistedFoodItem.timeAdded.hour in 6..12) {
                    val list = map[0] ?: mutableListOf()
                    list.add(persistedFoodItem.toDomainModel(foodItemDateAddedFormatter))
                    map[0] = list
                }

                if (persistedFoodItem.timeAdded.hour in 13..16) {
                    val list = map[1] ?: mutableListOf()
                    list.add(persistedFoodItem.toDomainModel(foodItemDateAddedFormatter))
                    map[1] = list
                }

                if (persistedFoodItem.timeAdded.hour in 17..24) {
                    val list = map[2] ?: mutableListOf()
                    list.add(persistedFoodItem.toDomainModel(foodItemDateAddedFormatter))
                    map[2] = list
                }

                if (persistedFoodItem.timeAdded.hour in 0..6) {
                    val list = map[2] ?: mutableListOf()
                    list.add(persistedFoodItem.toDomainModel(foodItemDateAddedFormatter))
                    map[2] = list
                }
            }

            _foodItems.emit(map)
        }

    }
}