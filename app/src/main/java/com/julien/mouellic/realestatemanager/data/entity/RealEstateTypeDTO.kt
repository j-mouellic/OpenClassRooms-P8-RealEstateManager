package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.PropertyMapper
import com.julien.mouellic.realestatemanager.data.mapper.RealEstateTypeMapper
import com.julien.mouellic.realestatemanager.domain.model.Property
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType

/**
 * Data class representing a Real Estate Type in the database.
 *
 * - Maps to the "real_estate_types" table in Room.
 * - Stores types of real estate, e.g., apartment, house, studio, etc.
 * - Each property can reference a RealEstateTypeDTO via its realEstateTypeId field.
 *
 * Functions:
 * - `toModel()`: Converts this DTO to a domain model `RealEstateType` using `RealEstateTypeMapper`.
 */
@Entity(
    tableName = "real_estate_types"
)
data class RealEstateTypeDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Long? = 0,

    @ColumnInfo(name = "name")
    val name: String,
){
    @Ignore
    fun toModel(): RealEstateType {
        return RealEstateTypeMapper().dtoToModel(this)
    }
}