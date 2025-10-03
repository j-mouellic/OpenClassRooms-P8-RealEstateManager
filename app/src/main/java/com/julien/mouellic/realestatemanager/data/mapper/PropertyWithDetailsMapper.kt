package com.julien.mouellic.realestatemanager.data.mapper


import com.julien.mouellic.realestatemanager.data.entity.PropertyDTO
import com.julien.mouellic.realestatemanager.data.flatten.PropertyWithDetails
import com.julien.mouellic.realestatemanager.domain.model.Property

/**
 * Mapper for converting between PropertyWithDetails (data flatten) and Property (domain model).
 *
 * - PropertyWithDetails contains a full representation of a property with all related entities:
 *   agent, location, real estate type, pictures, and commodities.
 * - Useful for views like SHOW or EDIT where all property details are needed.
 *
 * Functions:
 * - `modelToDto(property: Property)`: Converts a domain Property into a PropertyWithDetails flatten DTO,
 *   including all related entities as DTOs.
 * - `dtoToModel(dto: PropertyWithDetails)`: Converts a PropertyWithDetails flatten DTO back into a domain Property,
 *   reconstructing all nested domain models (agent, location, realEstateType, pictures, commodities).
 *
 * Purpose:
 * - Bridges the data layer and domain layer while keeping the full property details accessible.
 * - Respects Clean Architecture by separating persistence objects (DTOs) from domain models.
 */
class PropertyWithDetailsMapper {

    fun modelToDto(property: Property): PropertyWithDetails {
        return PropertyWithDetails(
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
            pictures = property.id?.let { propertyId ->
                property.pictures.map { it.toDTO(propertyId) }
            } ?: emptyList(),
            realEstateType = property.realEstateType?.toDTO(),
            commodities = property.commodities.map { it.toDTO() },
        )
    }

    fun dtoToModel(dto: PropertyWithDetails): Property {
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