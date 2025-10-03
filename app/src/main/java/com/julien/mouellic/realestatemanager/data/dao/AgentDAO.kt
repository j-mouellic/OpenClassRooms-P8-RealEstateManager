package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.mouellic.realestatemanager.data.entity.AgentDTO

/**
 * DAO (Data Access Object) for the Agent entity.
 * Provides database operations for agents.
 *
 * - `insert` adds a single agent, ignoring conflicts.
 * - `insertAll` adds multiple agents, ignoring conflicts.
 * - `update` modifies an existing agent.
 * - `delete` removes an agent.
 * - `getAllAgents` fetches all agents from the database.
 * - `getAgentById` fetches a single agent by its ID.
 */
@Dao
interface AgentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(agent: AgentDTO): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(agents: List<AgentDTO>): List<Long>

    @Update
    suspend fun update(agent: AgentDTO) :Int

    @Delete
    suspend fun delete(agent: AgentDTO)

    @Query("SELECT * FROM agents")
    suspend fun getAllAgents(): List<AgentDTO>

    @Query("SELECT * FROM agents WHERE id = :id")
    suspend fun getAgentById(id: Long): AgentDTO?
}