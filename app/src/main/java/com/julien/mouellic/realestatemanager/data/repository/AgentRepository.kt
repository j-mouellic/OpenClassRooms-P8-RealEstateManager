package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.AgentDAO
import com.julien.mouellic.realestatemanager.domain.model.Agent
import javax.inject.Inject

/**
 * Repository for accessing Agent data from the database.
 *
 * Responsibilities:
 * - Provides a clean API for the domain layer to perform CRUD operations on Agent entities.
 * - Converts between domain models (Agent) and data layer DTOs (AgentDTO).
 * - Wraps operations in `Result` objects when using AsResult methods, allowing
 *   safe exception handling and easier error propagation.
 *
 * Example usage of AsResult methods:
 * val result = agentRepository.getByIdAsResult(1L)
 * result.onSuccess { agent -> ... }
 * result.onFailure { error -> ... }
 */
class AgentRepository @Inject constructor(private val agentDAO: AgentDAO) {

    /** INSERT **/
    @WorkerThread
    suspend fun insert(agent: Agent): Long {
        return agentDAO.insert(agent.toDTO())
    }

    @WorkerThread
    suspend fun insertAll(agents: List<Agent>): List<Long> {
        return agentDAO.insertAll(agents.map { it.toDTO() })
    }

    @WorkerThread
    suspend fun insertAsResult(agent: Agent): Result<Long> {
        return try {
            Result.success(insert(agent))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(agent: Agent) {
        agentDAO.update(agent.toDTO())
    }

    @WorkerThread
    suspend fun updateAsResult(agent: Agent): Result<Unit> {
        return try {
            update(agent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/
    @WorkerThread
    suspend fun delete(agent: Agent) {
        agentDAO.delete(agent.toDTO())
    }

    @WorkerThread
    suspend fun deleteAsResult(agent: Agent): Result<Unit> {
        return try {
            delete(agent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET BY ID **/
    @WorkerThread
    suspend fun getById(id: Long): Agent? {
        return agentDAO.getAgentById(id)?.toModel()
    }

    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Agent?> {
        return try {
            Result.success(getById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET ALL **/
    @WorkerThread
    suspend fun getAll(): List<Agent> {
        return agentDAO.getAllAgents().map { it.toModel() }
    }

    @WorkerThread
    suspend fun getAllAsResult(): Result<List<Agent>> {
        return try {
            Result.success(getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}