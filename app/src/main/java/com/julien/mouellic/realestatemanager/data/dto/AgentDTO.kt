package com.julien.mouellic.realestatemanager.data.dto

data class AgentDTO(
    val id : Long?,
    val firstName : String,
    val lastName : String,
    val email : String,
    val phoneNumber : String,
    val realEstateAgency : String
)
