package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.EasyPropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

/**
 * Use Case: UpdateEasyPropertyUseCase
 *
 * Handles the business logic for updating a property in the "EasyProperty" repository.
 * Acts as an intermediary between the UI layer and the repository,
 * encapsulating the update logic without exposing repository details.
 */
class UpdateEasyPropertyUseCase @Inject constructor(
    private val easyPropertyRepository: EasyPropertyRepository
) {

    /**
     * Updates the given property in the repository.
     *
     * @param property The Property object to be updated.
     */
    suspend operator fun invoke(property: Property) {
        // Delegate the update operation to the repository
        easyPropertyRepository.update(property)
    }
}
