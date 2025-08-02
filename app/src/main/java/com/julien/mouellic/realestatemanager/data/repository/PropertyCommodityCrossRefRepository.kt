package com.julien.mouellic.realestatemanager.data.repository

import com.julien.mouellic.realestatemanager.data.dao.PropertyCommodityCrossRefDAO
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import kotlinx.coroutines.flow.Flow

class PropertyCommodityCrossRefRepository(
    private val dao: PropertyCommodityCrossRefDAO
) {

    /** INSERT **/
    suspend fun insertCrossRef(crossRef: PropertyCommodityCrossRefDTO): Long {
        return dao.insert(crossRef)
    }

    /** DELETE **/
    suspend fun deleteCrossRef(crossRef: PropertyCommodityCrossRefDTO) {
        dao.delete(crossRef)
    }

    suspend fun deleteByPropertyId(propertyId: Long) {
        dao.deleteByPropertyId(propertyId)
    }

    suspend fun deleteByCommodityId(commodityId: Long) {
        dao.deleteByCommodityId(commodityId)
    }

    suspend fun deleteByIds(propertyId: Long, commodityId: Long) {
        dao.delete(propertyId, commodityId)
    }

    /** GET (suspend) **/
    suspend fun getByPropertyId(propertyId: Long): List<PropertyCommodityCrossRefDTO> {
        return dao.getByPropertyId(propertyId)
    }

    suspend fun getByCommodityId(commodityId: Long): List<PropertyCommodityCrossRefDTO> {
        return dao.getByCommodityId(commodityId)
    }

    suspend fun getByIds(propertyId: Long, commodityId: Long): PropertyCommodityCrossRefDTO? {
        return dao.getById(propertyId, commodityId)
    }

    /** GET (real-time Flow) **/
    fun getByPropertyIdFlow(propertyId: Long): Flow<List<PropertyCommodityCrossRefDTO>> {
        return dao.getByPropertyIdRT(propertyId)
    }

    fun getByCommodityIdFlow(commodityId: Long): Flow<List<PropertyCommodityCrossRefDTO>> {
        return dao.getByCommodityIdRT(commodityId)
    }

    fun getByIdsFlow(propertyId: Long, commodityId: Long): Flow<PropertyCommodityCrossRefDTO?> {
        return dao.getByIdRT(propertyId, commodityId)
    }
}
