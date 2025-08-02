package com.julien.mouellic.realestatemanager.utils

import android.graphics.Bitmap
import android.graphics.Canvas

object BitmapUtils {

    fun create(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            val canvas = Canvas(this)
            canvas.drawColor(android.graphics.Color.WHITE)
        }
    }

    fun resize(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        var newWidth = maxWidth
        var newHeight = maxHeight

        if (bitmap.width > maxWidth) {
            newWidth = maxWidth
            newHeight = (newWidth / aspectRatio).toInt()
        }

        if (newHeight > maxHeight) {
            newHeight = maxHeight
            newWidth = (newHeight * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
    }
}