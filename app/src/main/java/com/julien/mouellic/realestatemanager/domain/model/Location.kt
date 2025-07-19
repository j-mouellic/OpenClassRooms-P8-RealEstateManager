package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.entity.LocationDTO
import com.julien.mouellic.realestatemanager.data.mapper.LocationMapper

data class Location(
    val id : Long?,
    val city : String,
    val postalCode : String,
    val street : String,
    val streetNumber : Int?,
    val country : String?,
    val longitude : Double?,
    val latitude : Double?
){
    @Ignore
    fun toDTO(): LocationDTO {
        return LocationMapper().modelToDto(this)
    }
}
