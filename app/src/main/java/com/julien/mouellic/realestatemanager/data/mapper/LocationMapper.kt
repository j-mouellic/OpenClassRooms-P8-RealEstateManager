package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.LocationDTO
import com.julien.mouellic.realestatemanager.domain.model.Location

/**
 * Mapper for converting between LocationDTO (data layer) and Location (domain layer).
 *
 * - `modelToDto(location: Location)`: Converts a domain model to a DTO for database operations.
 * - `dtoToModel(dto: LocationDTO)`: Converts a DTO from the database to a domain model.
 *
 * Purpose:
 * - Ensures separation of concerns between the data and domain layers.
 * - Supports Clean Architecture by preventing direct dependency of domain models on the database entities.
 */
class LocationMapper {

    fun modelToDto(location: Location): LocationDTO {
        return LocationDTO(
            id = location.id,
            city = location.city,
            postalCode = location.postalCode,
            street = location.street,
            streetNumber = location.streetNumber,
            country =  location.country ?: "",
            longitude = location.longitude,
            latitude = location.latitude,
        )
    }

    fun dtoToModel(dto: LocationDTO): Location {
        return Location(
            id = dto.id,
            city = dto.city,
            postalCode = dto.postalCode,
            street = dto.street,
            streetNumber = dto.streetNumber,
            country = dto.country,
            longitude = dto.longitude,
            latitude = dto.latitude,
        )
    }
}