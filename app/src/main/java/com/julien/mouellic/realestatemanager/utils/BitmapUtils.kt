package com.julien.mouellic.realestatemanager.utils

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * BitmapUtils
 *
 * Utility object for common bitmap operations such as creation and resizing.
 * Useful for handling property pictures in the app.
 */
object BitmapUtils {

    /**
     * Create a new blank Bitmap with the given width and height.
     * The bitmap is filled with white color by default.
     *
     * @param width Width of the bitmap in pixels
     * @param height Height of the bitmap in pixels
     * @return Bitmap instance
     */
    fun create(width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            val canvas = Canvas(this)
            canvas.drawColor(android.graphics.Color.WHITE) // Fill with white background
        }
    }

    /**
     * Resize a bitmap to fit within the given maxWidth and maxHeight
     * while maintaining the original aspect ratio.
     *
     * @param bitmap Original bitmap
     * @param maxWidth Maximum width
     * @param maxHeight Maximum height
     * @return Resized bitmap
     */
    fun resize(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        var newWidth = maxWidth
        var newHeight = maxHeight

        // Adjust width based on maxWidth while keeping aspect ratio
        if (bitmap.width > maxWidth) {
            newWidth = maxWidth
            newHeight = (newWidth / aspectRatio).toInt()
        }

        // Adjust height if it exceeds maxHeight
        if (newHeight > maxHeight) {
            newHeight = maxHeight
            newWidth = (newHeight * aspectRatio).toInt()
        }

        // Create the scaled bitmap
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
    }
}
