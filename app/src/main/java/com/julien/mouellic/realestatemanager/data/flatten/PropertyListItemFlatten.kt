package com.julien.mouellic.realestatemanager.data.flatten


import android.graphics.Bitmap
import com.julien.mouellic.realestatemanager.domain.model.*
import org.threeten.bp.Instant
import java.io.Serializable

data class PropertyListItemFlatten(
    // from Property
    val id: Long,
    val name: String,
    val description: String?,
    val surface: Double?,
    val nbRooms: Int?,
    val price: Double?,
    val isSold: Boolean,
    val dateCreation: Instant,
    val entryDate: Instant?,
    val saleDate: Instant?,

    // from EstateType
    val type: String?,

    // from Location
    val street: String?,
    val postalCode : String?,
    val city:String,
    val country:String,
    val longitude:Double?,
    val latitude:Double?,

    // from Agent
    val agentName: String?,

    // from Commodity
    val commoditiesType: List<String>,
    val commoditiesIds : List<Long>,

    // from Picture
    val picture: Bitmap?
) : Serializable {
    fun toModel(): Property {
        val typeDM = RealEstateType(
            id = null,
            name = type ?: "",
        )
        val agentDM = Agent(
            id = null,
            firstName = agentName ?: "",
            lastName = "",
            email= "",
            phoneNumber = "",
            realEstateAgency = ""
        )
        val locationDM = Location(
            id = null,
            street = street?:"",
            streetNumber = null,
            postalCode = postalCode?:"",
            city = city,
            country = country,
            longitude = longitude,
            latitude = latitude
        )
        val pictureDM = if(picture != null) listOf(
            Picture(
                id = null,
                content = picture,
                thumbnailContent = picture,
                order = 0
            )
        ) else emptyList()
        val commoditiesDM = commoditiesType.map { Commodity(
            id = null,
            name = it
        )
        }
        return Property(
            id = id,
            name = name,
            description = description,
            surface = surface,
            numbersOfRooms = nbRooms,
            numbersOfBathrooms = null,
            numbersOfBedrooms = null,
            apartmentNumber = null,
            price = price,
            isSold = isSold,
            creationDate = dateCreation,
            entryDate = entryDate,
            saleDate = saleDate,
            realEstateType = typeDM,
            location = locationDM,
            agent = agentDM,
            commodities = commoditiesDM,
            pictures = pictureDM
        )
    }
}

