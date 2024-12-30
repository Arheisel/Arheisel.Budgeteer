package com.arheisel.budgeteer.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

class Converters {

    @TypeConverter
    fun fromUnixSeconds(value: Long?) : ZonedDateTime? {
        return value?.let {
            val i = Instant.ofEpochSecond(it)
            return@let ZonedDateTime.ofInstant(i, ZoneOffset.UTC)
        }
    }

    @TypeConverter
    fun toUnixSeconds(value: ZonedDateTime?) : Long? {
        return value?.toEpochSecond()
    }
}