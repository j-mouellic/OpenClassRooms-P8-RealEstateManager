package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.CommodityDTO
import com.julien.mouellic.realestatemanager.data.mapper.CommodityMapper

data class Commodity(
    val id: Long,
    val name: String,
){
    @Ignore
    fun toDto(): CommodityDTO {
        return CommodityMapper().modelToDto(this)
    }
}
