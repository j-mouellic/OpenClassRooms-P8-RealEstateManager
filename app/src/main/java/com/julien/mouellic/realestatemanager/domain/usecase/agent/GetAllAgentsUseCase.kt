package com.julien.mouellic.realestatemanager.domain.usecase.agent

import com.julien.mouellic.realestatemanager.data.repository.AgentRepository
import com.julien.mouellic.realestatemanager.domain.model.Agent
import javax.inject.Inject

class GetAllAgentsUseCase @Inject constructor(
    private val agentRepository: AgentRepository
) {
    suspend operator fun invoke(): Result<List<Agent>> {
        return try {
            Result.success(agentRepository.getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}