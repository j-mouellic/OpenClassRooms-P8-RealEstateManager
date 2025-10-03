package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.CommodityDTO
import com.julien.mouellic.realestatemanager.domain.model.Commodity

/**
 * Mapper for converting between CommodityDTO (data layer) and Commodity (domain layer).
 *
 * - `modelToDto(commodity: Commodity)`: Converts a domain model to a DTO for database operations.
 * - `dtoToModel(dto: CommodityDTO)`: Converts a DTO from the database to a domain model.
 *
 * Purpose:
 * - Ensures separation of concerns between the data and domain layers.
 * - Supports Clean Architecture by preventing direct dependency of domain models on the database entities.
 */
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