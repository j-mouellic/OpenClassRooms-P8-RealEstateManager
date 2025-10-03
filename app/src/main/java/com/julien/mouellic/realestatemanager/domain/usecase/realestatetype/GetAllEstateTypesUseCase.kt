package com.julien.mouellic.realestatemanager.domain.usecase.realestatetype

import com.julien.mouellic.realestatemanager.data.repository.RealEstateTypeRepository
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import javax.inject.Inject

/**
 * Use Case: GetAllEstateTypesUseCase
 *
 * Responsible for retrieving all real estate types from the repository.
 * Encapsulates the business logic for fetching real estate types,
 * providing a clean API for the UI or other layers.
 */
class GetAllEstateTypesUseCase @Inject constructor(
    private val realEstateTypeRepository: RealEstateTypeRepository
) {

    /**
     * Invokes the use case to fetch all estate types.
     *
     * @return Result containing a list of RealEstateType on success,
     *         or an exception on failure.
     */
    suspend operator fun invoke(): Result<List<RealEstateType>> {
        return try {
            // Delegates the data retrieval to the repository
            Result.success(realEstateTypeRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
