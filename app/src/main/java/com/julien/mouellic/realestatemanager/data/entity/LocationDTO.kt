package com.julien.mouellic.realestatemanager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.julien.mouellic.realestatemanager.data.mapper.LocationMapper
import com.julien.mouellic.realestatemanager.domain.model.Location

/**
 * Data class representing a Location in the database.
 *
 * - Maps to the "locations" table in Room.
 * - Contains address details: city, postal code, street, street number, country,
 *   and optional coordinates (longitude, latitude).
 * - Multiple properties can share the same LocationDTO (1 Location → N Properties).
 * - For buildings with multiple units, differentiation is done via the
 *   `apartmentNumber` field in PropertyDTO.
 *
 * Functions:
 * - `toModel()`: Converts this DTO to a domain model `Location` using `LocationMapper`.
 */
@Entity(
    tableName = "locations"
)
data class LocationDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
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
