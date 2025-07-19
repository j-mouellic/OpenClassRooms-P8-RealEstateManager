package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.LocationMapper
import com.julien.mouellic.realestatemanager.domain.model.Location

@Entity(
    tableName = "locations"
)
data class LocationDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    val id : Long? = 0,

    @ColumnInfo(name = "city")
    val city : String,

    @ColumnInfo(name = "postal_code")
    val postalCode : String,

    @ColumnInfo(name = "street")
    val street : String,

    @ColumnInfo(name = "street_number")
    val streetNumber : Int?,

    @ColumnInfo(name = "country")
    val country : String,

    @ColumnInfo(name = "longitude")
    val longitude : Double?,

    @ColumnInfo(name = "latitude")
    val latitude : Double?
){
    @Ignore
    fun toModel(): Location {
        return LocationMapper().dtoToModel(this)
    }
}
