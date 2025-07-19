package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.mouellic.realestatemanager.data.entity.RealEstateTypeDTO
import kotlinx.coroutines.flow.Flow


@Dao
interface RealEstateTypeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(realEstateType: RealEstateTypeDTO): Long

    @Update
    suspend fun update(realEstateType: RealEstateTypeDTO): Int

    @Delete
    suspend fun delete(realEstateType: RealEstateTypeDTO)

    @Query("SELECT * FROM real_estate_types")
    fun getAll(): Flow<List<RealEstateTypeDTO>>

    @Query("SELECT * FROM real_estate_types WHERE real_estate_type_id = :id")
    suspend fun getById(id: Long): RealEstateTypeDTO?
}

