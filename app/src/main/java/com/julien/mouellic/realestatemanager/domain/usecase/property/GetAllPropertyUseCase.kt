package com.julien.mouellic.realestatemanager.domain.usecase.property

import com.julien.mouellic.realestatemanager.data.repository.PropertyRepository
import com.julien.mouellic.realestatemanager.domain.model.Property
import javax.inject.Inject

/**
 * Use Case: GetAllPropertyUseCase
 *
 * Handles fetching all properties from the repository.
 * Wraps the result in a [Result] to safely manage success and failure.
 */
class GetAllPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    /**
     * Executes the use case.
     * @return Result containing the list of properties on success, or an exception on failure.
     */
    suspend operator fun invoke(): Result<List<Property>> {
        return try {
            Result.success(propertyRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
