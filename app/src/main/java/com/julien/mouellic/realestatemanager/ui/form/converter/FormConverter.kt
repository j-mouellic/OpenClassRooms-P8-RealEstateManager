package com.julien.mouellic.realestatemanager.ui.form.converter


class FormConverter {

    fun toString(value: String): String? {
        return value.ifBlank {
            null
        }
    }

    fun toDouble(value: String): Double? {
        return value.ifBlank {
            null
        }?.toDouble()
    }

    fun toInt(value: String): Int? {
        return value.ifBlank {
            null
        }?.toInt()
    }

    fun toLong(value: String): Long? {
        return value.ifBlank {
            null
        }?.toLong()
    }

    fun toBoolean(value: String): Boolean? {
        return value.ifBlank {
            null
        }?.toBoolean()
    }

    fun toFloat(value: String): Float? {
        return value.ifBlank {
            null
        }?.toFloat()
    }

    fun toByte(value: String): Byte? {
        return value.ifBlank {
            null
        }?.toByte()
    }

    fun toShort(value: String): Short? {
        return value.ifBlank {
            null
        }?.toShort()
    }

    fun toChar(value: String): Char? {
        return value.ifBlank {
            null
        }?.first()
    }

    fun toByteArray(value: String): ByteArray? {
        return value.ifBlank {
            null
        }?.toByteArray()
    }

}