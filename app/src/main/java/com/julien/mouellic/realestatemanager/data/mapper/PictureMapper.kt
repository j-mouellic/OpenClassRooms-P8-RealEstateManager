package com.julien.mouellic.realestatemanager.data.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.julien.mouellic.realestatemanager.data.dto.PictureDTO
import com.julien.mouellic.realestatemanager.domain.model.Picture
import java.io.ByteArrayOutputStream
import java.util.Base64

class PictureMapper {

    fun toDTO(picture: Picture): PictureDTO {
        return PictureDTO(
            id = picture.id,
            description = picture.description,
            content = picture.content.toBase64(),
            thumbnailContent = picture.thumbnailContent.toBase64(),
            order = picture.order
        )
    }

    fun toModel(picture : PictureDTO) : Picture{
        val convertedContent = picture.content?.toBitmap() ?: throw IllegalStateException("Invalid Base64 for content")
        val convertedThumbnailContent = picture.thumbnailContent?.toBitmap() ?: throw IllegalStateException("Invalid Base64 for thumbnailContent")

        return Picture(
            id = picture.id,
            description = picture.description,
            content = convertedContent,
            thumbnailContent = convertedThumbnailContent,
            order = picture.order
        )
    }


    private fun Bitmap.toBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray);
    }

    private fun String.toBitmap(): Bitmap? {
        return try {
            val decodedBytes = Base64.getDecoder().decode(this)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

}