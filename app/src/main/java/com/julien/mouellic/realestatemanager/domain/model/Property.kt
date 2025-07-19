package com.julien.mouellic.realestatemanager.domain.model

import androidx.room.Ignore
import com.julien.mouellic.realestatemanager.data.mapper.PropertyMapper
import org.threeten.bp.Instant

data class Property(
    val id : Long?,
    val name : String,
    val description : String?,
    val surface : Double?,
    val numbersOfRooms : Int?,
    val numbersOfBathrooms : Int?,
    val numbersOfBedrooms : Int?,
    val price : Double?,
    val isSold : Boolean,
    val creationDate : Instant,
    val entryDate : Instant?,
    val saleDate : Instant?,
    val apartmentNumber : Int?,
    val location: Location?,
    val agent : Agent?,
    val type : RealEstateType?,
    val commodities : List<Commodity>,
    val pictures : List<Picture>
){
    @Ignore
    fun toDto(): Property {
        return PropertyMapper().modelToDto(this)
    }
}
