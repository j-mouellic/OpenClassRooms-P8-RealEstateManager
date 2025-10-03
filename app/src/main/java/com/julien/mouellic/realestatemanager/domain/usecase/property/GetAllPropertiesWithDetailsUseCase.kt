package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyWithDetailsRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

/**
 * Use Case: GetAllPropertiesWithDetailsUseCase
 *
 * Encapsulates the logic to fetch all properties along with their full details,
 * including location, agent, commodities, and pictures.
 */
class GetAllPropertiesWithDetailsUseCase @Inject constructor(
    private val propertyWithDetailsRepository: PropertyWithDetailsRepository
) {
    /**
     * Returns a list of all properties with their details.
     */
    suspend operator fun invoke(): List<Property> {
        return propertyWithDetailsRepository.getAll()
    }
}
