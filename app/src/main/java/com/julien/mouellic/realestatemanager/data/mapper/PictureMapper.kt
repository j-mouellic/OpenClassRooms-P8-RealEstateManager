package com.julien.mouellic.realestatemanager.data.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.julien.mouellic.realestatemanager.data.entity.PictureDTO
import com.julien.mouellic.realestatemanager.domain.model.Picture
import java.io.ByteArrayOutputStream
import java.util.Base64

class PictureMapper {

    fun modelToDTO(picture: Picture): PictureDTO {
        return PictureDTO(
            id = picture.id,
            description = picture.description,
            content = picture.content.toBase64(),
            thumbnailContent = picture.thumbnailContent.toBase64(),
            order = picture.order
        )
    }

    fun dtoToModel(picture : PictureDTO) : Picture{
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
}