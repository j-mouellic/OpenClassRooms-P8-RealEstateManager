package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.dto.AgentDTO
import com.julien.mouellic.realestatemanager.data.dto.LocationDTO
import com.julien.mouellic.realestatemanager.domain.model.Agent
import com.julien.mouellic.realestatemanager.domain.model.Location

class LocationMapper {

    fun toDto(location: Location): LocationDTO {
        return LocationDTO(
            id = location.id,
            city = location.city,
            postalCode = location.postalCode,
            street = location.street,
            streetNumber = location.streetNumber,
            country = location.country,
            longitude = location.longitude,
            latitude = location.latitude,
        )
    }

    fun toModel(location: LocationDTO): Location {
        return Location(
            id = location.id,
            city = location.city,
            postalCode = location.postalCode,
            street = location.street,
            streetNumber = location.streetNumber,
            country = location.country,
            longitude = location.longitude,
            latitude = location.latitude,
        )
    }
}