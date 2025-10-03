package com.julien.mouellic.realestatemanager.domain.model

import android.graphics.Bitmap
import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.PictureDTO
import com.julien.mouellic.realestatemanager.data.mapper.PictureMapper

/**
 * Domain model representing a Picture associated with a property.
 *
 * @param id Unique identifier for the picture (nullable for new pictures)
 * @param content Full-size image as a Bitmap (nullable)
 * @param thumbnailContent Thumbnail version of the image as a Bitmap (nullable)
 * @param order Position of the picture in the property's gallery (0-based index)
 */
data class Picture(
    val id: Long?,
    val content: Bitmap?,
    val thumbnailContent: Bitmap?,
    val order: Int
) {
    /**
     * Convert this domain model to its corresponding database DTO.
     *
     * @param propertyId The ID of the property this picture belongs to
     * @return PictureDTO ready to be inserted or updated in the database
     */
    @Ignore
    fun toDTO(propertyId: Long): PictureDTO {
        return PictureMapper().modelToDTO(this, propertyId)
    }
}
