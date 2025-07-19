package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.julien.mouellic.realestatemanager.data.entity.CommodityDTO

@Dao
interface CommodityDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(commodity: CommodityDTO): Long

    @Update
    suspend fun update(commodity: CommodityDTO)

    @Delete
    suspend fun delete(commodity: CommodityDTO)

    @Query("SELECT * FROM commodities")
    suspend fun getAllCommodities(): List<CommodityDTO>

    @Query("SELECT * FROM commodities WHERE commodity_id = :id")
    suspend fun getCommodityById(id: Long): CommodityDTO?
}