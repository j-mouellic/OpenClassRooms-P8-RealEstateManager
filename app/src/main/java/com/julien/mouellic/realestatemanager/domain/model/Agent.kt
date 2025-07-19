package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.AgentDTO
import com.julien.mouellic.realestatemanager.data.mapper.AgentMapper

data class Agent(
    val id : Long?,
    val firstName : String,
    val lastName : String,
    val email : String,
    val phoneNumber : String,
    val realEstateAgency : String
){
    @Ignore
    fun toDto(): AgentDTO {
        return AgentMapper().modelToDto(this)
    }
}
