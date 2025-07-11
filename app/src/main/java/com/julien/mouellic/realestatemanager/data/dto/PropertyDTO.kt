package com.julien.mouellic.realestatemanager.data.dto

import com.julien.mouellic.realestatemanager.domain.model.Commodity

data class PropertyDTO(
    val id : Long,
    val name : String,
    val description : String,
    val surface : Double,
    val numbersOfRooms : Int,
    val numbersOfBathrooms : Int,
    val numbersOfBedrooms : Int,
    val price : Double,
    val isSold : Boolean,
    val creationDate : String,
    val entryDate : String,
    val saleDate : String,
    val apartmentNumber : Int,
    val type : String,
    val locationId : Long?,
    val agentId : Long?,
    val commodities : List<Commodity>,
    val pictures : List<PictureDTO>
)
