package com.julien.mouellic.realestatemanager.data.converter

import androidx.room.TypeConverter

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