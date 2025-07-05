package com.julien.mouellic.realestatemanager.domain.models

data class Location(
    val city : String,
    val postalCode : String,
    val street : String,
    val streetNumber : Int,
    val country : String,
    val longitude : String,
    val latitude : String
)
