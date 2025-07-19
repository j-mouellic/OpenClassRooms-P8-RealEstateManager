package com.julien.mouellic.realestatemanager.domain.model

import android.graphics.Bitmap
import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.PictureDTO
import com.julien.mouellic.realestatemanager.data.mapper.PictureMapper

data class Picture(
    val id : Long,
    val content : Bitmap,
    val thumbnailContent : Bitmap,
    val order: Int
){
    @Ignore
    fun toDTO(): PictureDTO {
        return PictureMapper().modelToDTO(this)
    }
}
