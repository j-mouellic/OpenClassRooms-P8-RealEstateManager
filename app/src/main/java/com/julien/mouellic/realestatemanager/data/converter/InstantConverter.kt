package com.julien.mouellic.realestatemanager.data.converter

import androidx.room.TypeConverter
import org.threeten.bp.Instant

/**
 * A Room type converter that handles the conversion
 * between Instant objects and Long for database storage.
 *
 * - `toLong` converts an Instant into a Long (milliseconds since epoch) to save in the DB.
 * - `fromLong` converts a Long back into an Instant when reading from the DB.
 */
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