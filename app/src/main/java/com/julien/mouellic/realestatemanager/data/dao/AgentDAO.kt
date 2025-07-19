package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.mouellic.realestatemanager.data.entity.AgentDTO

@Dao
interface AgentDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(agent: AgentDTO): Long

    @Update
    suspend fun update(agent: AgentDTO)

    @Delete
    suspend fun delete(agent: AgentDTO)

    @Query("SELECT * FROM agents")
    suspend fun getAllAgents(): List<AgentDTO>

    @Query("SELECT * FROM agents WHERE agent_id = :id")
    suspend fun getAgentById(id: Long): AgentDTO?
}