package com.julien.mouellic.realestatemanager.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.PictureMapper
import com.julien.mouellic.realestatemanager.domain.model.Picture

@Entity(
    tableName = "pictures",
    foreignKeys = [
        ForeignKey(
            entity = PropertyDTO::class,
            parentColumns = ["id"],
            childColumns = ["property_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PictureDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "picture_id")
    val id : Long?,

    @ColumnInfo(name = "content")
    val content : Bitmap,

    @ColumnInfo(name = "thumbnail_content")
    val thumbnailContent : Bitmap, // TODO : column info

    @ColumnInfo(name = "order")
    val order: Int,

    @ColumnInfo(name = "property_id")
    val propertyId : Long
){
    @Ignore
    fun toModel(): Picture {
        return PictureMapper().dtoToModel(this)
    }
}
