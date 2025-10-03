package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyWithDetailsRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

/**
 * Use Case: GetPropertyWithDetailsUseCase
 *
 * Fetches a single property along with all its details (location, agent, commodities, pictures)
 * from the repository using its ID.
 */
class GetPropertyWithDetailsUseCase @Inject constructor(
    private val propertyWithDetailsRepository: PropertyWithDetailsRepository
) {
    /**
     * Executes the use case.
     * @param propertyId ID of the property to fetch.
     * @return The property with all details, or null if not found.
     */
    suspend operator fun invoke(propertyId: Long): Property? {
        return propertyWithDetailsRepository.getById(propertyId)
    }
}
