package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.CommodityMapper
import com.julien.mouellic.realestatemanager.domain.model.Commodity

/**
 * Data class representing a Commodity in the database.
 *
 * - Maps to the "commodities" table in Room.
 * - Contains a primary key `id` and a `name`.
 *
 * Functions:
 * - `toModel()`: Converts this DTO to a domain model `Commodity` using `CommodityMapper`.
 */
@Entity(
    tableName = "commodities"
)
data class CommodityDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Long? = 0,

    @ColumnInfo(name = "name")
    val name: String,
){
    @Ignore
    fun toModel(): Commodity {
        return CommodityMapper().dtoToModel(this)
    }
}
