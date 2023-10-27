package com.stickebox.common

import io.realm.kotlin.types.RealmInstant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun RealmInstant.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(epochSeconds, nanosecondsOfSecond, ZoneOffset.UTC)
}

fun LocalDateTime.toRealmInstant(): RealmInstant {
    return RealmInstant.from(this.toEpochSecond(ZoneOffset.UTC), this.nano)
}