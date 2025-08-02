package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.julien.mouellic.realestatemanager.data.entity.PropertyWithDetailsDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyWithDetailsDAO {

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :id")
    fun getByIdRT(id: Long): Flow<PropertyWithDetailsDTO?>

    @Transaction
    @Query("SELECT * FROM properties")
    fun getAllRT(): Flow<List<PropertyWithDetailsDTO>>

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getById(id: Long): PropertyWithDetailsDTO?

    @Transaction
    @Query("SELECT * FROM properties")
    suspend fun getAll(): List<PropertyWithDetailsDTO>
}