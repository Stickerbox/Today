package com.stickebox.common

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val config =
    RealmConfiguration.create(schema = setOf(PersistedTodoItem::class, PersistedFoodItem::class))
val realm: Realm = Realm.open(config)

interface Persistable

@Single
class Database {
    suspend fun <T> save(item: T) where T : Persistable, T : RealmObject {
        realm.write {
            copyToRealm(item)
        }
    }

    inline fun <reified T> query(
        query: String? = null
    ): RealmResults<T> where T : Persistable, T : RealmObject {
        if (query != null) {
            return realm.query<T>(query).find()
        }
        return realm.query<T>().find()
    }

    fun close() {
        realm.close()
    }
}

@Single
class Repository(
    private val database: Database
) {

    fun getFoodItems(day: LocalDateTime): Flow<List<FoodItem>> {
        val foodItemDateAddedFormatter = DateTimeFormatter.ofPattern("h:mm a")

        return database.query<PersistedFoodItem>()
            .asFlow()
            .filter { it is InitialResults }
            .map { it as InitialResults }
            .map { it.list }
            .map { it.distinct() }
            .map { persistedFoodItems ->
                persistedFoodItems.map {
                    it.toDomainModel(foodItemDateAddedFormatter)
                }.filter {
                    it.timeAddedLocalDateTime.dayOfYear == day.dayOfYear
                            && it.timeAddedLocalDateTime.year == day.year
                }
            }
    }

    suspend fun saveFoodItem(foodItem: FoodItem) {
        database.save(foodItem.toPersistedModel())
    }

    fun getTodoItems(currentTime: LocalDateTime, dateTimeFormatter: DateTimeFormatter): Flow<List<TodoItem>> {
        val initial = database.query<PersistedTodoItem>()
            .asFlow()
            .filter { it is InitialResults }
            .map { it as InitialResults }
            .map { it.list }
            .map { it.distinct() }
            .map { persistedTodoItems ->
                persistedTodoItems.map {
                    it.toDomainModel(currentTime, dateTimeFormatter)
                }.filter { item ->
                    item.toTime.dayOfYear == currentTime.dayOfYear
                            && item.toTime.year == currentTime.year
                }
            }

        val updates = database.query<PersistedTodoItem>()
            .asFlow()
            .filter { it is UpdatedResults }
            .map { it as UpdatedResults }
            .map { it.list }
            .map { it.distinct() }
            .map { persistedTodoItems ->
                persistedTodoItems.map {
                    it.toDomainModel(currentTime, dateTimeFormatter)
                }.filter { item ->
                    item.toTime.dayOfYear == currentTime.dayOfYear
                            && item.toTime.year == currentTime.year
                }
            }

        return merge(initial, updates)
    }

    suspend fun saveTodoItem(todoItem: TodoItem) {
        database.save(todoItem.toPersistedModel())
    }
}
