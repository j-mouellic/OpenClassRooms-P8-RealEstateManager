package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

class SearchPropertiesUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
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
