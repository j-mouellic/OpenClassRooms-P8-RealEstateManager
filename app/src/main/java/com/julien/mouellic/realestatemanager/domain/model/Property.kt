package com.julien.mouellic.realestatemanager.domain.model

import java.time.Instant

data class Property(
    val id : Long,
    val name : String,
    val description : String,
    val surface : Double,
    val numbersOfRooms : Int,
    val numbersOfBathrooms : Int,
    val numbersOfBedrooms : Int,
    val price : Double,
    val isSold : Boolean,
    val creationDate : Instant,
    val entryDate : Instant,
    val saleDate : Instant,
    val apartementNumber : Int,
    val type : EstateType,
    val location : Location,
    val agent : Agent,
    val commodities : List<Commodity>,
    val pictures : List<Picture>
)
