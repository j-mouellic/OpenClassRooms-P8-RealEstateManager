package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.julien.mouellic.realestatemanager.data.entity.PropertyCommodityCrossRefDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyCommodityCrossRefDAO {
    /** INSERT **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(propertyCommodityCrossRef: PropertyCommodityCrossRefDTO): Long

    /** DELETE **/
    @Query("DELETE FROM property_commodity WHERE property_id = :propertyId")
    suspend fun deleteByPropertyId(propertyId: Long)

    @Query("DELETE FROM property_commodity WHERE commodity_id = :commodityId")
    suspend fun deleteByCommodityId(commodityId: Long)

    @Query("DELETE FROM property_commodity WHERE property_id = :propertyId AND commodity_id = :commodityId")
    suspend fun delete(propertyId: Long, commodityId: Long)

    @Delete
    suspend fun delete(propertyCommodityCrossRef: PropertyCommodityCrossRefDTO)


    /** SELECT SUSPENDED **/
    @Query("SELECT * FROM property_commodity WHERE property_id = :propertyId")
    suspend fun getByPropertyId(propertyId: Long): List<PropertyCommodityCrossRefDTO>

    @Query("SELECT * FROM property_commodity WHERE commodity_id = :commodityId")
    suspend fun getByCommodityId(commodityId: Long): List<PropertyCommodityCrossRefDTO>

    @Query("SELECT * FROM property_commodity WHERE property_id = :propertyId AND commodity_id = :commodityId")
    suspend fun getById(propertyId: Long, commodityId: Long): PropertyCommodityCrossRefDTO?

    /** SELECT REAL TIME / FLOW **/
    @Query("SELECT * FROM property_commodity WHERE property_id = :propertyId")
    fun getByPropertyIdRT(propertyId: Long): Flow<List<PropertyCommodityCrossRefDTO>>

    @Query("SELECT * FROM property_commodity WHERE commodity_id = :commodityId")
    fun getByCommodityIdRT(commodityId: Long): Flow<List<PropertyCommodityCrossRefDTO>>

    @Query("SELECT * FROM property_commodity WHERE property_id = :propertyId AND commodity_id = :commodityId")
    fun getByIdRT(propertyId: Long, commodityId: Long): Flow<PropertyCommodityCrossRefDTO?>
}