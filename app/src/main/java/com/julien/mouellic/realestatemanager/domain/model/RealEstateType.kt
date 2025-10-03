package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.RealEstateTypeDTO
import com.julien.mouellic.realestatemanager.data.mapper.RealEstateTypeMapper

/**
 * Domain model representing a type of real estate property.
 * Examples include Apartment, House, Loft, Studio, Villa, etc.
 *
 * @param id Unique identifier of the real estate type (nullable for new entries)
 * @param name Name of the property type (e.g., "Apartment", "House")
 */
data class RealEstateType(
    val id: Long?,
    val name: String,
) {
    /**
     * Converts this domain model into its corresponding database DTO.
     * This DTO is ready to be persisted in the Room database.
     *
     * @return RealEstateTypeDTO representation of this RealEstateType
     */
    @Ignore
    fun toDTO(): RealEstateTypeDTO {
        return RealEstateTypeMapper().modelToDto(this)
    }
}
