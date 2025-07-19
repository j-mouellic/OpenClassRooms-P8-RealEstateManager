package com.julien.mouellic.realestatemanager.data.converter


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class BitmapConverter {

    companion object {
        private const val COMPRESSION_QUALITY = 100
        private const val START_OFFSET = 0
        private val FORMAT = Bitmap.CompressFormat.PNG
    }

    @TypeConverter
    fun toBitmap(bytes: ByteArray?): Bitmap? {
        return bytes?.let { BitmapFactory.decodeByteArray(it, START_OFFSET, bytes.size) }
    }

    @TypeConverter
    fun fromBitmap(img: Bitmap?): ByteArray? {
        return img?.let {
            val os = ByteArrayOutputStream()
            img.compress(FORMAT, COMPRESSION_QUALITY, os)
            os.toByteArray()
        }
    }
}