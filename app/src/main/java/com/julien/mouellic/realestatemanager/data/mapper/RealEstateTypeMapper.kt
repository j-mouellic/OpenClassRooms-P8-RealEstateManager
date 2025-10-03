package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.RealEstateTypeDTO
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType

/**
 * Mapper for converting between RealEstateType domain model and RealEstateTypeDTO (data layer).
 *
 * - `modelToDto(realEstateType: RealEstateType)`: Converts a domain RealEstateType into a DTO for persistence.
 * - `dtoToModel(realEstateType: RealEstateTypeDTO)`: Converts a DTO from the database back into the domain model.
 *
 * Purpose:
 * - Ensures separation between the domain layer and data layer.
 * - Allows the app to follow Clean Architecture principles by keeping domain models independent from persistence objects.
 */
class RealEstateTypeMapper {

    fun modelToDto(realEstateType : RealEstateType): RealEstateTypeDTO {
        return RealEstateTypeDTO(
            id = realEstateType.id,
            name = realEstateType.name
        )
    }

    fun dtoToModel(realEstateType: RealEstateTypeDTO): RealEstateType {
        return RealEstateType(
            id = realEstateType.id,
            name = realEstateType.name
        )
    }
}