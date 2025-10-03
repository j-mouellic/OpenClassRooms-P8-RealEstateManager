package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.mouellic.realestatemanager.data.entity.RealEstateTypeDTO
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing real estate types.
 *
 * - Handles CRUD operations for `RealEstateTypeDTO`.
 * - Provides both real-time Flow and suspended queries.
 *
 * Functions:
 * - `insert(realEstateType)`: Adds a new type, ignores if it already exists.
 * - `update(realEstateType)`: Updates an existing type.
 * - `delete(realEstateType)`: Deletes a type.
 * - `getAllAsFlow()`: Returns all types as a Flow (real-time updates).
 * - `getAll()`: Returns all types once.
 * - `getById(id)`: Returns a single type by its ID.
 */
@Dao
interface RealEstateTypeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(realEstateType: RealEstateTypeDTO): Long

    @Update
    suspend fun update(realEstateType: RealEstateTypeDTO): Int

    @Delete
    suspend fun delete(realEstateType: RealEstateTypeDTO)

    @Query("SELECT * FROM real_estate_types")
    fun getAllAsFlow(): Flow<List<RealEstateTypeDTO>>

    @Query("SELECT * FROM real_estate_types")
    suspend fun getAll(): List<RealEstateTypeDTO>

    @Query("SELECT * FROM real_estate_types WHERE id = :id")
    suspend fun getById(id: Long): RealEstateTypeDTO?
}

