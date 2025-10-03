package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyRepository
import javax.inject.Inject

/**
 * Use Case: DeletePropertyUseCase
 *
 * Handles the business logic for deleting a property by its ID.
 * Encapsulates the deletion operation in a dedicated class for testability and separation of concerns.
 */
class DeletePropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    /**
     * Deletes a property by its ID.
     *
     * @param propertyId The ID of the property to delete
     */
    suspend operator fun invoke(propertyId: Long) {
        propertyRepository.deleteById(propertyId)
    }
}
