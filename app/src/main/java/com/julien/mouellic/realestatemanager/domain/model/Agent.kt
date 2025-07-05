package com.julien.mouellic.realestatemanager.domain.models

data class Agent(
    val id : Long,
    val firstName : String,
    val lastName : String,
    val email : String,
    val phoneNumber : String,
    val realEstateAgency : String
)
