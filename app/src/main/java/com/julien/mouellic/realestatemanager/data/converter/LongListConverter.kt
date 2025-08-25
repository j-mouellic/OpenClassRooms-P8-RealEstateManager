package com.julien.mouellic.realestatemanager.data.converter

import androidx.room.TypeConverter

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
