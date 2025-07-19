package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.RealEstateTypeDTO
import com.julien.mouellic.realestatemanager.data.mapper.RealEstateTypeMapper

data class RealEstateType(
    val id: Long?,
    val name: String,
){
    @Ignore
    fun toDTO(): RealEstateTypeDTO {
        return RealEstateTypeMapper().modelToDto(this)
    }
}
