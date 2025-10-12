package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import kotlinx.coroutines.flow.Flow
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
     * Executes the use case reactively.
     *
     * Returns a Flow that emits the list of properties matching the given filters,
     * and automatically updates whenever the database content changes.
     */
    operator fun invoke(
        type: Long? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minSurface: Double? = null,
        maxSurface: Double? = null,
        minNbRooms: Int? = null,
        maxNbRooms: Int? = null,
        isAvailable: Boolean? = null,
        commodities: List<Long>? = null
    ): Flow<List<Property>> {
        return propertyRepository.searchAsFlow(
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

    suspend fun searchOnce(
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
        return propertyRepository.searchOnce(
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
