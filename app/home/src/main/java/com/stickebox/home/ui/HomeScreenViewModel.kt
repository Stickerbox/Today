package com.stickebox.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stickebox.common.Database
import com.stickebox.common.PersistedTodoItem
import com.stickebox.common.Repository
import com.stickebox.common.TodoItem
import com.stickebox.common.toDomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

//val TEST_TODO_ITEMS = listOf(
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 9, 0, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 11, 0, 0),
//        text = "Build Android TODO app"
//    ),
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 11, 0, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 12, 0, 0),
//        text = "French learning"
//    ),
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 12, 0, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 16, 0, 0),
//        text = "Continue building Android TODO app"
//    ),
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 16, 0, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 16, 30, 0),
//        text = "Eat"
//    ),
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 16, 30, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 17, 30, 0),
//        text = "Apply for jobs"
//    ),
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 17, 30, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 18, 0, 0),
//        text = "Walk Ruby and meet Li"
//    ),
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 18, 0, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 20, 30, 0),
//        text = "Eat and relax"
//    ),
//    PersistedTodoItem(
//        fromTime = LocalDateTime.of(2023, 10, 24, 20, 30, 0),
//        toTime = LocalDateTime.of(2023, 10, 24, 0, 0, 0),
//        text = "Continue building Android TODO app"
//    )
//)

@KoinViewModel
class HomeScreenViewModel(
    repository: Repository
) : ViewModel() {
    private val _todoItems: MutableStateFlow<List<TodoItem>> = MutableStateFlow(emptyList())
    val todoItems: StateFlow<List<TodoItem>>
        get() = _todoItems

    private val headerFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
    val currentDate: String = LocalDateTime.now().format(headerFormatter)

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

    init {
        val currentTime = LocalDateTime.now()

        viewModelScope.launch {
//            _todoItems.emit(TEST_TODO_ITEMS.map { item ->
//                item.toDomainModel(currentTime, dateTimeFormatter)
//            })
        }
    }

    fun saveTodoItem(fromTime: LocalDateTime, toTime: LocalDateTime, text: String) {

    }
}
