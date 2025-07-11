package com.julien.mouellic.realestatemanager.data.mapper

import com.julien.mouellic.realestatemanager.data.dto.AgentDTO
import com.julien.mouellic.realestatemanager.domain.model.Agent

class AgentMapper {

    fun toDto(agent : Agent) : AgentDTO {
        return AgentDTO(
            id = agent.id,
            firstName =  agent.firstName,
            lastName = agent.lastName,
            email = agent.email,
            phoneNumber = agent.phoneNumber,
            realEstateAgency = agent.realEstateAgency
        )
    }

    fun toModel(agent : AgentDTO) : Agent {
        return Agent(
            id = agent.id,
            firstName =  agent.firstName,
            lastName = agent.lastName,
            email = agent.email,
            phoneNumber = agent.phoneNumber,
            realEstateAgency = agent.realEstateAgency
        )
    }
}