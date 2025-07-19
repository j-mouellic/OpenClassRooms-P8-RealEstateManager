package com.julien.mouellic.realestatemanager.data.converter

import androidx.room.TypeConverter
import org.threeten.bp.Instant

class InstantConverter {
    @TypeConverter
    fun fromLong(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun toLong(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }
}