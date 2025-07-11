package com.julien.mouellic.realestatemanager.data.dto

import android.graphics.Bitmap

data class PictureDTO(
    val id : Long,
    val description : String?,
    val content : String?,
    val thumbnailContent : String?,
    val order: Int
)