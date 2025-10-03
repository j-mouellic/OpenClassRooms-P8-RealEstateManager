package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.mouellic.realestatemanager.data.entity.PictureDTO
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) for the Picture entity.
 * Handles database operations related to property pictures.
 *
 * - `insert` adds a new picture or replaces it if there’s a conflict.
 * - `update` modifies an existing picture.
 * - `delete` removes a picture.
 * - `deleteUnused` removes pictures not linked to any property.
 * - `getAll` returns a Flow of all pictures.
 * - `getById` retrieves a single picture by its ID.
 * - `getForProperty` returns a Flow of pictures for a specific property.
 * - `deleteByPropertyId` removes all pictures linked to a specific property.
 */
@Dao
interface PictureDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(picture: PictureDTO): Long

    @Update
    suspend fun update(picture: PictureDTO): Int

    @Delete
    suspend fun delete(picture: PictureDTO)

    @Query("DELETE FROM pictures WHERE property_id NOT IN (SELECT property_id FROM properties)")
    suspend fun deleteUnused()

    @Query("SELECT * FROM pictures")
    fun getAll(): Flow<List<PictureDTO>>

    @Query("SELECT * FROM pictures WHERE id = :id")
    suspend fun getById(id: Long): PictureDTO?

    @Query("SELECT * FROM pictures WHERE property_id = :propertyId")
    fun getForProperty(propertyId: Long): Flow<List<PictureDTO>>

    @Query("DELETE FROM pictures WHERE property_id = :propertyId")
    suspend fun deleteByPropertyId(propertyId: Long)
}
