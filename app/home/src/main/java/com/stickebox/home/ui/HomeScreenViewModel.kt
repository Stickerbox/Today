package com.stickebox.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.common.Repository
import com.stickebox.common.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@KoinViewModel
class HomeScreenViewModel(
    private val repository: Repository
) : ViewModel() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

    val todoItems: Flow<List<TodoItem>> =
        repository.getTodoItems(LocalDateTime.now(), dateTimeFormatter)

    private val headerFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
    val currentDate: String = LocalDateTime.now().format(headerFormatter)

    fun saveTodoItem(fromTime: LocalDateTime, toTime: LocalDateTime, text: String) {
        val todoItem = TodoItem(
            time = "Do not care about this here",
            text = text,
            isCurrentItem = false,
            fromTime = fromTime,
            toTime = toTime
        )

        viewModelScope.launch {
            repository.saveTodoItem(todoItem)
        }
    }
}
