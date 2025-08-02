package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.PictureDTO
import com.julien.mouellic.realestatemanager.domain.model.Picture
import com.julien.mouellic.realestatemanager.data.converter.BitmapConverter;

class PictureMapper {

    fun modelToDTO(picture: Picture, propertyId: Long): PictureDTO {
        return PictureDTO(
            id = picture.id,
            content = picture.content.let { BitmapConverter().fromBitmap(it) } ?: ByteArray(0),
            thumbnailContent = picture.thumbnailContent.let { BitmapConverter().fromBitmap(it) } ?: ByteArray(0),
            order = picture.order,
            propertyId = propertyId
        )
    }

    fun dtoToModel(picture : PictureDTO) : Picture{
        val convertedContent = BitmapConverter().toBitmap(picture.content)
        val convertedThumbnailContent = BitmapConverter().toBitmap(picture.thumbnailContent)

        return Picture(
            id = picture.id,
            content = convertedContent,
            thumbnailContent = convertedThumbnailContent,
            order = picture.order
        )
    }
}