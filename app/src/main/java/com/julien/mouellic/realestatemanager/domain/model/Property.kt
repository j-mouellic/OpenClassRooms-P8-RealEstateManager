package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.PropertyDTO
import com.julien.mouellic.realestatemanager.data.mapper.PropertyMapper
import org.threeten.bp.Instant

/**
 * Domain model representing a real estate property.
 *
 * @param id Unique identifier of the property (nullable for new properties)
 * @param name Name or title of the property
 * @param description Optional description of the property
 * @param surface Optional surface area in square meters
 * @param numbersOfRooms Optional number of rooms
 * @param numbersOfBathrooms Optional number of bathrooms
 * @param numbersOfBedrooms Optional number of bedrooms
 * @param price Optional price of the property
 * @param isSold Whether the property has been sold
 * @param creationDate Date when the property was created in the system
 * @param entryDate Optional date when the property was listed
 * @param saleDate Optional date when the property was sold
 * @param apartmentNumber Optional apartment number if relevant
 * @param location Associated Location object (nullable)
 * @param agent Associated Agent object (nullable)
 * @param realEstateType Associated RealEstateType object (nullable)
 * @param commodities List of associated Commodities (e.g., park, shop)
 * @param pictures List of associated Pictures
 */
data class Property(
    val id: Long?,
    val name: String,
    val description: String?,
    val surface: Double?,
    val numbersOfRooms: Int?,
    val numbersOfBathrooms: Int?,
    val numbersOfBedrooms: Int?,
    val price: Double?,
    val isSold: Boolean,
    val creationDate: Instant,
    val entryDate: Instant?,
    val saleDate: Instant?,
    val apartmentNumber: Int?,
    val location: Location?,
    val agent: Agent?,
    val realEstateType: RealEstateType?,
    val commodities: List<Commodity>,
    val pictures: List<Picture>
) {
    /**
     * Converts this domain model into its corresponding database DTO.
     * This DTO is ready for persistence operations like insert or update.
     *
     * @return PropertyDTO representation of this Property
     */
    @Ignore
    fun toDTO(): PropertyDTO {
        return PropertyMapper().modelToDto(this)
    }
}
