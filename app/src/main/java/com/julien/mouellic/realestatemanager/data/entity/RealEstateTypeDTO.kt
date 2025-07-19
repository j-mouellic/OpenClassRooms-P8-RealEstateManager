package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.PropertyMapper
import com.julien.mouellic.realestatemanager.data.mapper.RealEstateTypeMapper
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType

@Entity(
    tableName = "real_estate_types"
)
data class RealEstateTypeDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "real_estate_type_id")
    val id : Long? = 0,

    @ColumnInfo(name = "name")
    val name: String,
){
    @Ignore
    fun toModel(): RealEstateType {
        return RealEstateTypeMapper().dtoToModel(this)
    }
}