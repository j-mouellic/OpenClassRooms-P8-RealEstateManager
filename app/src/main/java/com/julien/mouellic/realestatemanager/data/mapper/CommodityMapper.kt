package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.dto.AgentDTO
import com.julien.mouellic.realestatemanager.data.dto.CommodityDTO
import com.julien.mouellic.realestatemanager.domain.model.Agent
import com.julien.mouellic.realestatemanager.domain.model.Commodity

class CommodityMapper {

    fun toDto(commodity: Commodity): CommodityDTO {
        return CommodityDTO(
            id = commodity.id,
            name = commodity.name
        )
    }

    fun toModel(commodity: CommodityDTO): Commodity {
        return Commodity(
            id = commodity.id,
            name = commodity.name
        )
    }
}