package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.CommodityMapper
import com.julien.mouellic.realestatemanager.domain.model.Commodity

@Entity(
    tableName = "commodities"
)
data class CommodityDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "commodity_id")
    val id : Long? = 0,

    @ColumnInfo(name = "name")
    val name: String,
){
    @Ignore
    fun toModel(): Commodity {
        return CommodityMapper().dtoToModel(this)
    }
}
