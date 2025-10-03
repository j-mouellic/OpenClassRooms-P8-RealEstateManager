package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.CommodityDTO
import com.julien.mouellic.realestatemanager.data.mapper.CommodityMapper

/**
 * Domain model representing a Commodity.
 *
 * This class is part of the domain layer and decoupled from Room entities.
 * A commodity represents a nearby facility or feature (e.g., Park, School, Shop)
 * associated with a property.
 *
 * Fields:
 * @param id The unique identifier of the commodity (nullable when creating new commodities)
 * @param name The name of the commodity (e.g., "Park", "School")
 */
data class Commodity(
    val id: Long?,
    val name: String,
) {
    /**
     * Convert this domain model to its corresponding Data Transfer Object (DTO)
     * used by the database layer (Room).
     *
     * @return CommodityDTO equivalent of this Commodity
     *
     * @note @Ignore annotation prevents Room from trying to persist this method.
     */
    @Ignore
    fun toDTO(): CommodityDTO {
        return CommodityMapper().modelToDto(this)
    }
}
