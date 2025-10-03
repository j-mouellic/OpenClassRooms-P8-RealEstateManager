package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.EasyPropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

/**
 * Use Case: InsertEasyPropertyUseCase
 *
 * Handles the insertion of a property into the database using the EasyPropertyRepository.
 * Encapsulates the business logic for adding a new property.
 */
class InsertEasyPropertyUseCase @Inject constructor(
    private val easyPropertyRepository: EasyPropertyRepository
) {
    /**
     * Executes the use case.
     * @param property The property object to insert.
     * @return The ID of the newly inserted property.
     */
    suspend operator fun invoke(property: Property): Long {
        return easyPropertyRepository.insert(property)
    }
}
