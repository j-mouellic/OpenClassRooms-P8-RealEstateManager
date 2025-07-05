package com.julien.mouellic.realestatemanager.domain.models

import android.graphics.Bitmap

data class Picture(
    val id : Long,
    val description : String,
    val content : Bitmap
)
