package com.julien.mouellic.realestatemanager.data.repository

import com.julien.mouellic.realestatemanager.data.dao.PropertyCommodityCrossRefDAO
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import kotlinx.coroutines.flow.Flow

/**
 * Repository to manage the many-to-many relationship between properties and commodities.
 *
 * Responsibilities:
 * - Insert, delete, and query the cross-references between properties and commodities.
 * - Provide both synchronous (suspend) and reactive (Flow) access to the data.
 *
 * This repository operates on the PropertyCommodityCrossRefDAO and interacts with
 * PropertyCommodityCrossRefDTO objects, which represent the association between a property
 * and a commodity in the database.
 *
 * Example use-cases:
 * - Retrieve all commodities for a given property.
 * - Retrieve all properties that have a given commodity.
 * - Maintain the N:N relationship when properties or commodities are added, updated, or removed.
 */
class PropertyCommodityCrossRefRepository(
    private val dao: PropertyCommodityCrossRefDAO
) {

    /**
     * Insert a cross-reference between a property and a commodity.
     * Returns the row ID of the inserted record.
     */
    suspend fun insertCrossRef(crossRef: PropertyCommodityCrossRefDTO): Long {
        return dao.insert(crossRef)
    }

    /**
     * Delete a specific cross-reference between a property and a commodity.
     */
    suspend fun deleteCrossRef(crossRef: PropertyCommodityCrossRefDTO) {
        dao.delete(crossRef)
    }

    /**
     * Delete all cross-references for a given property.
     * Useful when deleting a property or clearing its commodities.
     */
    suspend fun deleteByPropertyId(propertyId: Long) {
        dao.deleteByPropertyId(propertyId)
    }

    /**
     * Delete all cross-references for a given commodity.
     * Useful when deleting a commodity or clearing its associated properties.
     */
    suspend fun deleteByCommodityId(commodityId: Long) {
        dao.deleteByCommodityId(commodityId)
    }

    /**
     * Delete a specific cross-reference by property ID and commodity ID.
     */
    suspend fun deleteByIds(propertyId: Long, commodityId: Long) {
        dao.delete(propertyId, commodityId)
    }

    /**
     * Get all cross-references for a specific property (suspend function).
     */
    suspend fun getByPropertyId(propertyId: Long): List<PropertyCommodityCrossRefDTO> {
        return dao.getByPropertyId(propertyId)
    }

    /**
     * Get all cross-references for a specific commodity (suspend function).
     */
    suspend fun getByCommodityId(commodityId: Long): List<PropertyCommodityCrossRefDTO> {
        return dao.getByCommodityId(commodityId)
    }

    /**
     * Get a specific cross-reference by property ID and commodity ID (suspend function).
     */
    suspend fun getByIds(propertyId: Long, commodityId: Long): PropertyCommodityCrossRefDTO? {
        return dao.getById(propertyId, commodityId)
    }

    /**
     * Get all cross-references for a specific property as a Flow.
     * Allows real-time observation of changes.
     */
    fun getByPropertyIdFlow(propertyId: Long): Flow<List<PropertyCommodityCrossRefDTO>> {
        return dao.getByPropertyIdRT(propertyId)
    }

    /**
     * Get all cross-references for a specific commodity as a Flow.
     * Allows real-time observation of changes.
     */
    fun getByCommodityIdFlow(commodityId: Long): Flow<List<PropertyCommodityCrossRefDTO>> {
        return dao.getByCommodityIdRT(commodityId)
    }

    /**
     * Get a specific cross-reference by property ID and commodity ID as a Flow.
     * Allows observing changes in real-time.
     */
    fun getByIdsFlow(propertyId: Long, commodityId: Long): Flow<PropertyCommodityCrossRefDTO?> {
        return dao.getByIdRT(propertyId, commodityId)
    }
}
