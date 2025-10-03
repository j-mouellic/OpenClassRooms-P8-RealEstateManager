package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.PropertyDTO
import com.julien.mouellic.realestatemanager.domain.model.Property

/**
 * Mapper for converting between PropertyDTO (data layer) and Property (domain layer).
 *
 * Functions:
 * - `modelToDto(property: Property)`: Converts a domain model Property into a DTO for database storage.
 *   Only stores IDs for related entities (agent, location, realEstateType) to maintain foreign key relationships.
 *
 * - `dtoToModel(propertyDTO: PropertyDTO)`: Converts a PropertyDTO from the database into a domain model Property.
 *   Related entities (agent, location, realEstateType, commodities, pictures) are set to null or empty,
 *   as they are not loaded by this simple DTO mapping.
 *
 * Purpose:
 * - Ensures separation between the data layer (Room database) and the domain layer (business logic).
 * - Respects Clean Architecture by isolating domain models from persistence details.
 * - Provides a simple way to store and retrieve Property data while handling foreign keys correctly.
 */
class PropertyMapper {

    fun modelToDto(property: Property): PropertyDTO {
        return PropertyDTO(
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
        )
    }

    fun dtoToModel(propertyDTO: PropertyDTO): Property {
        return Property(
            id = propertyDTO.id,
            name = propertyDTO.name,
            description = propertyDTO.description,
            surface = propertyDTO.surface,
            numbersOfRooms = propertyDTO.numbersOfRooms,
            numbersOfBathrooms = propertyDTO.numbersOfBathrooms,
            numbersOfBedrooms = propertyDTO.numbersOfBedrooms,
            price = propertyDTO.price,
            isSold = propertyDTO.isSold,
            creationDate = propertyDTO.creationDate,
            entryDate = propertyDTO.entryDate,
            saleDate = propertyDTO.saleDate,
            apartmentNumber = propertyDTO.apartmentNumber,
            agent = null,
            location = null,
            realEstateType = null,
            commodities = emptyList(),
            pictures = emptyList()
        )
    }
}