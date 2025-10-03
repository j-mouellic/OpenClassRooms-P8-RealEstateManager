package com.julien.mouellic.realestatemanager.data.converter

import androidx.room.TypeConverter

/**
 * A Room type converter that handles conversion between
 * List<String> and a single String for database storage.
 *
 * - `fromList` converts a List<String> into a single
 *   comma-separated String to save in the DB.
 * - `fromString` converts a comma-separated String back
 *   into a List<String> when reading from the DB.
 */
class ListConverter {

    companion object {
        private const val SEPARATOR = ","
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(SEPARATOR)
    }

    @TypeConverter
    fun fromString(data: String): List<String> {
        return data.split(SEPARATOR)
    }
}