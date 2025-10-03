package com.julien.mouellic.realestatemanager.data.converter

import androidx.room.TypeConverter

/**
 * A Room type converter that handles conversion between
 * List<Long> and a single String for database storage.
 *
 * - `fromList` converts a List<Long> into a comma-separated String to save in the DB.
 * - `fromString` converts a comma-separated String back into a List<Long> when reading from the DB, ignoring
 *   any invalid numbers.
 */
class LongListConverter {

    companion object {
        private const val SEPARATOR = ","
    }

    @TypeConverter
    fun fromList(list: List<Long>): String {
        return list.joinToString(SEPARATOR)
    }

    @TypeConverter
    fun fromString(data: String): List<Long> {
        return if (data.isBlank()) emptyList()
        else data.split(SEPARATOR).mapNotNull { it.toLongOrNull() }
    }
}
