package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.entity.AgentDTO
import com.julien.mouellic.realestatemanager.domain.model.Agent

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