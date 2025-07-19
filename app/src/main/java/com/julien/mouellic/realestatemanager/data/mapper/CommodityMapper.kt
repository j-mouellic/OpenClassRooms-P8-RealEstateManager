package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.CommodityDTO
import com.julien.mouellic.realestatemanager.domain.model.Commodity

class CommodityMapper {

    fun modelToDto(commodity: Commodity): CommodityDTO {
        return CommodityDTO(
            id = commodity.id,
            name = commodity.name
        )
    }

    fun dtoToModel(dto: CommodityDTO): Commodity {
        return Commodity(
            id = dto.id ?: 0L,
            name = dto.name
        )
    }
}