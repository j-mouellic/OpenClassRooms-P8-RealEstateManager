package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.AgentDTO
import com.julien.mouellic.realestatemanager.domain.model.Agent

/**
 * Mapper for converting between AgentDTO (data layer) and Agent (domain layer).
 *
 * - `modelToDto(agent: Agent)`: Converts a domain model to a DTO for database operations.
 * - `dtoToModel(dto: AgentDTO)`: Converts a DTO from the database to a domain model.
 *
 * Purpose:
 * - Ensures separation of concerns between the data and domain layers.
 * - Supports Clean Architecture by preventing direct dependency of domain models on the database entities.
 */
class AgentMapper {

    fun modelToDto(agent : Agent) : AgentDTO {
        return AgentDTO(
            id = agent.id,
            firstName =  agent.firstName,
            lastName = agent.lastName,
            email = agent.email,
            phoneNumber = agent.phoneNumber,
            realEstateAgency = agent.realEstateAgency
        )
    }

    fun dtoToModel(dto : AgentDTO) : Agent {
        return Agent(
            id = if (dto.id == 0L) null else dto.id,
            firstName =  dto.firstName,
            lastName = dto.lastName,
            email = dto.email,
            phoneNumber = dto.phoneNumber,
            realEstateAgency = dto.realEstateAgency
        )
    }
}