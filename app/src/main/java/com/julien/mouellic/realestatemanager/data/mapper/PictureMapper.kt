package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.PictureDTO
import com.julien.mouellic.realestatemanager.domain.model.Picture
import com.julien.mouellic.realestatemanager.data.converter.BitmapConverter;

/**
 * Mapper for converting between PictureDTO (data layer) and Picture (domain layer).
 *
 * Functions:
 * - `modelToDTO(picture: Picture, propertyId: Long)`: Converts a domain model Picture to a DTO for database storage.
 *   Uses BitmapConverter to transform Bitmap into ByteArray. Associates the picture with a property via propertyId.
 *
 * - `dtoToModel(picture: PictureDTO)`: Converts a PictureDTO from the database back into a domain model Picture.
 *   Uses BitmapConverter to convert ByteArray back into Bitmap.
 *
 * Purpose:
 * - Ensures separation between the data layer (database) and the domain layer (business logic).
 * - Respects Clean Architecture by preventing domain models from depending on database types.
 * - Handles conversion between Bitmap and ByteArray for image storage in Room.
 */
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