package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.dto.AgentDTO
import com.julien.mouellic.realestatemanager.data.dto.PropertyDTO
import com.julien.mouellic.realestatemanager.domain.model.Agent
import com.julien.mouellic.realestatemanager.domain.model.Property
import java.time.Instant

class PropertyMapper {

    fun toDto(property: Property): PropertyDTO {
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
        )
    }

    fun toModel(propertyDTO: PropertyDTO): Property {
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
        )
    }
}