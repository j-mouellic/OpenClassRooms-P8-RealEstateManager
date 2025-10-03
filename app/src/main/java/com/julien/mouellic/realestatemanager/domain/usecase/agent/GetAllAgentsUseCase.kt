package com.julien.mouellic.realestatemanager.domain.usecase.agent

import com.julien.mouellic.realestatemanager.data.repository.AgentRepository
import com.julien.mouellic.realestatemanager.domain.model.Agent
import javax.inject.Inject

/**
 * Use Case: GetAllAgentsUseCase
 *
 * In Clean Architecture, a Use Case represents a single, specific piece of business logic.
 * Its main responsibility is to orchestrate interactions between the repository (data layer)
 * and the domain models, without being concerned with how the data is stored or displayed.
 *
 * Responsibilities:
 * - Encapsulates the business logic of "getting all agents".
 * - Provides a clear API for the UI or other layers to execute this operation.
 * - Handles success and failure cases using Result<T>, so the caller can react accordingly.
 *
 * By isolating business logic in use cases, we:
 * - Keep UI layers (Activities, Fragments, ViewModels) free from business rules.
 * - Make logic easily testable in unit tests.
 * - Follow the Single Responsibility Principle (SRP) and Separation of Concerns (SoC).
 */
class GetAllAgentsUseCase @Inject constructor(
    private val agentRepository: AgentRepository
) {
    /**
     * Executes the use case to fetch all agents.
     *
     * @return Result<List<Agent>>: Success with list of agents or Failure with exception
     */
    suspend operator fun invoke(): Result<List<Agent>> {
        return try {
            Result.success(agentRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
