package com.julien.mouellic.realestatemanager.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.*
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import com.julien.mouellic.realestatemanager.domain.model.*
import javax.inject.Inject

class EasyPropertyRepository @Inject constructor(
    private val locationDAO: LocationDAO,
    private val propertyDAO: PropertyDAO,
    private val pictureDAO: PictureDAO,
    private val commodityDAO: CommodityDAO,
    private val propertyCommodityCrossRefDAO: PropertyCommodityCrossRefDAO
)  {

    /** INSERT **/
    @WorkerThread
    suspend fun insert(property: Property): Long {
        var propertyCopy = property.copy()
        if(property.location != null) {
            val location = property.location
            val locationID = locationDAO.search(location.street, location.streetNumber, location.postalCode, location.city, location.country ?: "")
            if(locationID == null) {
                val id = locationDAO.insert(location.toDTO())
                propertyCopy = propertyCopy.copy(location = location.copy(id = id))
            } else {
                propertyCopy = propertyCopy.copy(location = location.copy(id = locationID))
            }
        }
        val propertyId = propertyDAO.insert(propertyCopy.toDTO())
        propertyCopy.commodities.forEach { commodity ->
            val commodityId = commodityDAO.insert(commodity.toDTO())
            propertyCommodityCrossRefDAO.insert(PropertyCommodityCrossRefDTO(propertyId, commodityId))
        }
        propertyCopy.pictures.sortedBy { it.order }.forEachIndexed { index, picture ->
            pictureDAO.insert(picture.copy(order = index).toDTO(propertyId))
        }
        locationDAO.deleteUnused()
        pictureDAO.deleteUnused()
        return propertyId
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(property: Property) {
        if (property.id == null) {
            throw IllegalArgumentException("Property ID must not be null")
        }
        val propertyId: Long = property.id?:0
        var propertyCopy = property.copy()
        if (property.location != null) {
            val location = property.location
            val locationID = locationDAO.search(
                location.street,
                location.streetNumber,
                location.postalCode,
                location.city,
                location.country ?: ""
            )
            if(locationID == null) {
                val id = locationDAO.insert(location.toDTO())
                propertyCopy = propertyCopy.copy(location = location.copy(id = id))
            } else {
                propertyCopy = propertyCopy.copy(location = location.copy(id = locationID))
            }
        }
        propertyDAO.update(propertyCopy.toDTO())
        propertyCommodityCrossRefDAO.deleteByPropertyId(propertyId)
        propertyCopy.commodities.forEach { commodity ->
            val commodityId = commodityDAO.insert(commodity.toDTO())
            propertyCommodityCrossRefDAO.insert(
                PropertyCommodityCrossRefDTO(
                    propertyId,
                    commodityId
                )
            )
        }
        pictureDAO.deleteByPropertyId(propertyId)
        propertyCopy.pictures.sortedBy { it.order }.forEachIndexed { index, picture ->
            pictureDAO.insert(picture.copy(order = index).toDTO(propertyId))
        }
        locationDAO.deleteUnused()
        pictureDAO.deleteUnused()
    }

    /** DELETE **/
    @WorkerThread
    suspend fun delete(property: Property) {
        if (property.id == null) {
            throw IllegalArgumentException("Property ID must not be null")
        }
        val propertyId: Long = property.id?:0
        propertyCommodityCrossRefDAO.deleteByPropertyId(propertyId)
        pictureDAO.deleteByPropertyId(propertyId)
        propertyDAO.deleteById(propertyId)
        locationDAO.deleteUnused()
        pictureDAO.deleteUnused()
    }

}