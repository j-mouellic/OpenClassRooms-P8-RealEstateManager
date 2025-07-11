package com.julien.mouellic.realestatemanager.data.dto

data class LocationDTO(
    val id : Long?,
    val city : String,
    val postalCode : String,
    val street : String,
    val streetNumber : Int?,
    val country : String?,
    val longitude : Double?,
    val latitude : Double?
)
