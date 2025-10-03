package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.LocationDTO
import com.julien.mouellic.realestatemanager.data.mapper.LocationMapper

/**
 * Domain model representing a Location.
 *
 * Represents the physical location of a property including address and coordinates.
 *
 * @param id Unique identifier (nullable for new locations)
 * @param city City name
 * @param postalCode Postal code
 * @param street Street name
 * @param streetNumber Street number (nullable)
 * @param country Country name (nullable)
 * @param longitude Longitude coordinate (nullable)
 * @param latitude Latitude coordinate (nullable)
 */
data class Location(
    val id: Long?,
    val city: String,
    val postalCode: String,
    val street: String,
    val streetNumber: Int?,
    val country: String?,
    val longitude: Double?,
    val latitude: Double?
) {
    /**
     * Convert this domain model to its corresponding database DTO.
     */
    @Ignore
    fun toDTO(): LocationDTO {
        return LocationMapper().modelToDto(this)
    }
}
