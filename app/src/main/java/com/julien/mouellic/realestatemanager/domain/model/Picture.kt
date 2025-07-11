package com.julien.mouellic.realestatemanager.domain.model

import android.graphics.Bitmap

data class Picture(
    val id : Long,
    val description : String?,
    val content : Bitmap,
    val thumbnailContent : Bitmap,
    val order: Int
)
