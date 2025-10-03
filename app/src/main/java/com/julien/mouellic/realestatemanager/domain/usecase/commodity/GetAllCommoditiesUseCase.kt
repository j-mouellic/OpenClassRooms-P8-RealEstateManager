package com.julien.mouellic.realestatemanager.domain.usecase.commodity

import com.julien.mouellic.realestatemanager.data.repository.CommodityRepository
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import javax.inject.Inject

/**
 * Use Case: GetAllCommoditiesUseCase
 *
 * In Clean Architecture, a Use Case represents a single, specific piece of business logic.
 * Its main responsibility is to orchestrate interactions between the repository (data layer)
 * and the domain models, without being concerned with how the data is stored or displayed.
 *
 * Responsibilities:
 * - Encapsulates the business logic of "getting all commodities".
 * - Provides a clear API for the UI or other layers to execute this operation.
 * - Handles success and failure cases using Result<T>, so the caller can react accordingly.
 *
 * Benefits:
 * - Keeps UI layers free from business rules.
 * - Makes logic easily testable in unit tests.
 * - Follows Single Responsibility Principle (SRP) and Separation of Concerns (SoC).
 */
class GetAllCommoditiesUseCase @Inject constructor(
    private val commodityRepository: CommodityRepository
) {
    /**
     * Executes the use case to fetch all commodities.
     *
     * @return Result<List<Commodity>>: Success with list of commodities or Failure with exception
     */
    suspend operator fun invoke(): Result<List<Commodity>> {
        return try {
            Result.success(commodityRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
