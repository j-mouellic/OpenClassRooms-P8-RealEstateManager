package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.mouellic.realestatemanager.data.entity.CommodityDTO

@Dao
interface CommodityDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(commodity: CommodityDTO): Long

    @Update
    suspend fun update(commodity: CommodityDTO) :Int

    @Delete
    suspend fun delete(commodity: CommodityDTO)

    @Query("SELECT * FROM commodities")
    suspend fun getAll(): List<CommodityDTO>

    @Query("SELECT * FROM commodities WHERE id = :id")
    suspend fun getById(id: Long): CommodityDTO?
}