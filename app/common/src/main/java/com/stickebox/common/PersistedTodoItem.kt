package com.stickebox.common

import io.realm.kotlin.internal.toDuration
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * @param fromTime The time that this item starts
 * @param toTime The time that this item ends
 * @param text The content of what this item wants the user to do
 */
class PersistedTodoItem: Persistable, RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId()
    var fromTime: RealmInstant = RealmInstant.now()
    var toTime: RealmInstant = RealmInstant.now()
    var text: String = ""
}

fun TodoItem.toPersistedModel(): PersistedTodoItem {
    val realmFromTime = this.fromTime.toRealmInstant()
    val realmToTime = this.toTime.toRealmInstant()
    return PersistedTodoItem().apply {
        fromTime = realmFromTime
        toTime = realmToTime
        text = this@toPersistedModel.text
    }
}

fun PersistedTodoItem.toDomainModel(
    currentTime: LocalDateTime,
    dateTimeFormatter: DateTimeFormatter
): TodoItem {
    val fromTimeLocalDateTime = fromTime.toLocalDateTime()
    val toTimeLocalDateTime = toTime.toLocalDateTime()

    val isCurrentTime = currentTime.isBefore(toTimeLocalDateTime) && currentTime.isAfter(fromTimeLocalDateTime)
//        fromTimeLocalDateTime.isAfter(currentTime) && toTimeLocalDateTime.isBefore(currentTime)

    return TodoItem(
        time = "${
            dateTimeFormatter.format(fromTimeLocalDateTime).lowercase(
                Locale.ENGLISH
            )
        } - ${dateTimeFormatter.format(toTimeLocalDateTime).lowercase(Locale.ENGLISH)}",
        text = text,
        isCurrentItem = isCurrentTime,
        fromTime = fromTimeLocalDateTime,
        toTime = toTimeLocalDateTime
    )
}
