package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.AgentDTO
import com.julien.mouellic.realestatemanager.data.mapper.AgentMapper

/**
 * Domain model representing a Real Estate Agent.
 *
 * This class is part of the domain layer and is decoupled from Room entities.
 * It contains only the business-relevant fields for an agent.
 *
 * Fields:
 * @param id The unique identifier of the agent (nullable when creating new agents)
 * @param firstName Agent's first name
 * @param lastName Agent's last name
 * @param email Agent's email address
 * @param phoneNumber Agent's phone number
 * @param realEstateAgency The agency the agent works for
 */
data class Agent(
    val id : Long?,
    val firstName : String,
    val lastName : String,
    val email : String,
    val phoneNumber : String,
    val realEstateAgency : String
) {
    /**
     * Convert this domain model to its corresponding Data Transfer Object (DTO)
     * used by the database layer (Room).
     *
     * @return AgentDTO equivalent of this Agent
     *
     * @note @Ignore annotation prevents Room from trying to persist this method.
     */
    @Ignore
    fun toDTO(): AgentDTO {
        return AgentMapper().modelToDto(this)
    }
}
