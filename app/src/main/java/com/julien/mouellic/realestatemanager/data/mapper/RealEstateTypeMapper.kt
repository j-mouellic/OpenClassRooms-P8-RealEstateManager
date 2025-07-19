package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.RealEstateTypeDTO
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType

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