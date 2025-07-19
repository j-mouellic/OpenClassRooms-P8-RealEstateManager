package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.PropertyDTO
import com.julien.mouellic.realestatemanager.data.entity.PropertyWithDetailsDTO
import com.julien.mouellic.realestatemanager.domain.model.Property

class PropertyWithDetailsMapper {

    fun modelToDto(property: Property): PropertyWithDetailsDTO {
        return PropertyWithDetailsDTO(
            propertyEntity = PropertyDTO(
                id = property.id,
                name = property.name,
                description = property.description,
                surface = property.surface,
                numbersOfRooms = property.numbersOfRooms,
                numbersOfBathrooms = property.numbersOfBathrooms,
                numbersOfBedrooms = property.numbersOfBedrooms,
                price = property.price,
                isSold = property.isSold,
                creationDate = property.creationDate,
                entryDate = property.entryDate,
                saleDate = property.saleDate,
                apartmentNumber = property.apartmentNumber,
                agentId = property.agent?.id,
                locationId = property.location?.id,
                realEstateTypeId = property.realEstateType?.id,
            ),
            agent = property.agent?.toDTO(),
            location = property.location?.toDTO(),
            pictures = property.pictures.map { it.toDTO() },
            realEstateType = property.realEstateType?.toDTO(),
            commodities = property.commodities.map { it.toDTO() },
        )
    }

    fun dtoToModel(dto: PropertyWithDetailsDTO): Property {
        return Property(
            id = dto.propertyEntity.id,
            name = dto.propertyEntity.name,
            description = dto.propertyEntity.description,
            surface = dto.propertyEntity.surface,
            numbersOfRooms = dto.propertyEntity.numbersOfRooms,
            numbersOfBathrooms = dto.propertyEntity.numbersOfBathrooms,
            numbersOfBedrooms = dto.propertyEntity.numbersOfBedrooms,
            price = dto.propertyEntity.price,
            isSold = dto.propertyEntity.isSold,
            creationDate = dto.propertyEntity.creationDate,
            entryDate = dto.propertyEntity.entryDate,
            saleDate = dto.propertyEntity.saleDate,
            apartmentNumber = dto.propertyEntity.apartmentNumber,
            agent = dto.agent?.toModel(),
            location = dto.location?.toModel(),
            realEstateType = dto.realEstateType?.toModel(),
            commodities = dto.commodities?.map { it.toModel() } ?: emptyList(),
            pictures = dto.pictures?.map { it.toModel() } ?: emptyList()
        )
    }
}