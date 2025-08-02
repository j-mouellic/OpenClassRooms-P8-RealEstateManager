package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.LocationDTO
import com.julien.mouellic.realestatemanager.domain.model.Location

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