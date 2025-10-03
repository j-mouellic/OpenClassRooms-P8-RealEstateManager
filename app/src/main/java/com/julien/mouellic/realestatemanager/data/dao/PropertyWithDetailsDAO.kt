package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.julien.mouellic.realestatemanager.data.flatten.PropertyWithDetails
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing properties with all their related details.
 *
 * - `PropertyWithDetails` includes the property plus its location, agent, pictures, and commodities.
 * - All functions marked with @Transaction to ensure data consistency when fetching multiple tables.
 *
 * Functions:
 * - `getByIdRT(id: Long)`: Returns a Flow of a single property with details in real-time.
 * - `getAllRT()`: Returns a Flow of all properties with details in real-time.
 * - `getById(id: Long)`: Suspended function to fetch a single property with details once.
 * - `getAll()`: Suspended function to fetch all properties with details once.
 */
@Dao
interface PropertyWithDetailsDAO {

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :id")
    fun getByIdRT(id: Long): Flow<PropertyWithDetails?>

    @Transaction
    @Query("SELECT * FROM properties")
    fun getAllRT(): Flow<List<PropertyWithDetails>>

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getById(id: Long): PropertyWithDetails?

    @Transaction
    @Query("SELECT * FROM properties")
    suspend fun getAll(): List<PropertyWithDetails>
}