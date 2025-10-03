package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

/**
 * Use Case: SearchPropertiesUseCase
 *
 * Handles the business logic for searching properties based on multiple criteria.
 * Encapsulates filtering logic and delegates the actual data retrieval to PropertyRepository.
 */
class SearchPropertiesUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    /**
     * Executes the use case.
     *
     * @param type Optional property type filter (by real estate type ID)
     * @param minPrice Minimum property price filter
     * @param maxPrice Maximum property price filter
     * @param minSurface Minimum surface area filter
     * @param maxSurface Maximum surface area filter
     * @param minNbRooms Minimum number of rooms filter
     * @param maxNbRooms Maximum number of rooms filter
     * @param isAvailable Filter by availability (sold or not)
     * @param commodities Optional list of commodity IDs to filter properties having at least one of them
     *
     * @return List of properties matching the given criteria
     */
    suspend operator fun invoke(
        type: Long? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minSurface: Double? = null,
        maxSurface: Double? = null,
        minNbRooms: Int? = null,
        maxNbRooms: Int? = null,
        isAvailable: Boolean? = null,
        commodities: List<Long>? = null
    ): List<Property> {
        return propertyRepository.search(
            type,
            minPrice,
            maxPrice,
            minSurface,
            maxSurface,
            minNbRooms,
            maxNbRooms,
            isAvailable,
            commodities
        )
    }
}
