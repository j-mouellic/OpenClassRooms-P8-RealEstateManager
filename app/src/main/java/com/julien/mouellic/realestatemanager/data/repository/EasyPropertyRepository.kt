package com.julien.mouellic.realestatemanager.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.*
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import com.julien.mouellic.realestatemanager.domain.model.*
import javax.inject.Inject

/**
 * Repository to manage properties and their related entities.
 *
 * This repository handles the full cascade of operations across multiple tables:
 * - Properties
 * - Locations
 * - Pictures
 * - Commodities
 * - Property-Commodity cross-references
 *
 * It ensures consistency when inserting, updating, or deleting a property,
 * taking care of N:N and 1:N relationships automatically.
 */
class EasyPropertyRepository @Inject constructor(
    private val locationDAO: LocationDAO,
    private val propertyDAO: PropertyDAO,
    private val pictureDAO: PictureDAO,
    private val commodityDAO: CommodityDAO,
    private val propertyCommodityCrossRefDAO: PropertyCommodityCrossRefDAO
) {

    /**
     * Inserts a property along with its related entities.
     * Manages location reuse, commodity associations, and property pictures.
     */
    @WorkerThread
    suspend fun insert(property: Property): Long {
        var propertyCopy = property.copy()

        // --- Handle location ---
        if(property.location != null) {
            val location = property.location
            // Search for an existing location to avoid duplicates
            val locationID = locationDAO.search(location.street, location.streetNumber, location.postalCode, location.city, location.country ?: "")
            if(locationID == null) {
                // Insert new location if not found
                val id = locationDAO.insert(location.toDTO())
                propertyCopy = propertyCopy.copy(location = location.copy(id = id))
            } else {
                // Reuse existing location
                propertyCopy = propertyCopy.copy(location = location.copy(id = locationID))
            }
        }

        // --- Insert property ---
        val propertyId = propertyDAO.insert(propertyCopy.toDTO())

        // --- Handle commodities (N:N relationship) ---
        propertyCopy.commodities.forEach { commodity ->
            val commodityId = if (commodity.id != null) {
                val existing = commodityDAO.getById(commodity.id)
                existing?.id ?: commodityDAO.insert(commodity.toDTO())
            } else {
                commodityDAO.insert(commodity.toDTO())
            }
            // Insert cross-reference
            propertyCommodityCrossRefDAO.insert(PropertyCommodityCrossRefDTO(propertyId, commodityId))
        }

        // --- Handle pictures (1:N relationship) ---
        propertyCopy.pictures.sortedBy { it.order }.forEachIndexed { index, picture ->
            pictureDAO.insert(picture.copy(order = index).toDTO(propertyId))
        }

        // --- Cleanup unused locations and pictures ---
        locationDAO.deleteUnused()
        pictureDAO.deleteUnused()

        return propertyId
    }

    /**
     * Updates a property along with its related entities.
     * Ensures all associated tables are kept consistent.
     */
    @WorkerThread
    suspend fun update(property: Property) {
        if (property.id == null) {
            throw IllegalArgumentException("Property ID must not be null")
        }
        val propertyId: Long = property.id
        var propertyCopy = property.copy()

        // --- Handle location ---
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

        // --- Update property ---
        propertyDAO.update(propertyCopy.toDTO())

        // --- Update commodities (N:N) ---
        propertyCommodityCrossRefDAO.deleteByPropertyId(propertyId)
        propertyCopy.commodities.forEach { commodity ->
            val commodityId = commodityDAO.insert(commodity.toDTO())
            propertyCommodityCrossRefDAO.insert(PropertyCommodityCrossRefDTO(propertyId, commodityId))
        }

        // --- Update pictures (1:N) ---
        pictureDAO.deleteByPropertyId(propertyId)
        propertyCopy.pictures.sortedBy { it.order }.forEachIndexed { index, picture ->
            pictureDAO.insert(picture.copy(order = index).toDTO(propertyId))
        }

        // --- Cleanup unused locations and pictures ---
        locationDAO.deleteUnused()
        pictureDAO.deleteUnused()
    }

    /**
     * Deletes a property and all its related data.
     * Ensures cascading deletion for pictures and property-commodity relations.
     */
    @WorkerThread
    suspend fun delete(property: Property) {
        if (property.id == null) {
            throw IllegalArgumentException("Property ID must not be null")
        }
        val propertyId: Long = property.id

        // --- Delete N:N relations ---
        propertyCommodityCrossRefDAO.deleteByPropertyId(propertyId)

        // --- Delete 1:N pictures ---
        pictureDAO.deleteByPropertyId(propertyId)

        // --- Delete property itself ---
        propertyDAO.deleteById(propertyId)

        // --- Cleanup unused locations and pictures ---
        locationDAO.deleteUnused()
        pictureDAO.deleteUnused()
    }
}
