package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.PictureDAO
import com.julien.mouellic.realestatemanager.domain.model.Picture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository to manage CRUD operations for pictures associated with properties.
 *
 * Responsibilities:
 * - Insert, update, delete pictures in the database.
 * - Fetch single pictures or lists of pictures (either all pictures or those linked to a property).
 * - Provide safe `Result` wrappers to prevent crashes and propagate exceptions in a controlled manner.
 *
 * This repository interacts with the PictureDAO and converts between the domain model (Picture)
 * and the database DTO (PictureDTO).
 */
class PictureRepository @Inject constructor(private val pictureDAO: PictureDAO) {

    /**
     * Insert a picture into the database for a specific property.
     */
    @WorkerThread
    suspend fun insert(picture: Picture, propertyId: Long): Long {
        return pictureDAO.insert(picture.toDTO(propertyId))
    }

    /**
     * Insert a picture with exception handling using Result.
     * Useful to prevent crashes when inserting fails.
     */
    @WorkerThread
    suspend fun insertAsResult(picture: Picture, propertyId: Long): Result<Long> {
        return try {
            Result.success(insert(picture, propertyId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update an existing picture in the database.
     */
    @WorkerThread
    suspend fun update(picture: Picture, propertyId: Long) {
        pictureDAO.update(picture.toDTO(propertyId))
    }

    /**
     * Update a picture with exception handling using Result.
     */
    @WorkerThread
    suspend fun updateAsResult(picture: Picture, propertyId: Long): Result<Unit> {
        return try {
            update(picture, propertyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete a picture from the database.
     */
    @WorkerThread
    suspend fun delete(picture: Picture, propertyId: Long) {
        pictureDAO.delete(picture.toDTO(propertyId))
    }

    /**
     * Delete a picture with exception handling using Result.
     */
    @WorkerThread
    suspend fun deleteAsResult(picture: Picture, propertyId: Long): Result<Unit> {
        return try {
            delete(picture, propertyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get a picture by its ID.
     * @return the domain model Picture or null if not found.
     */
    @WorkerThread
    suspend fun getById(id: Long): Picture? {
        return pictureDAO.getById(id)?.toModel()
    }

    /**
     * Get a picture by ID with Result wrapper for safe error handling.
     */
    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Picture?> {
        return try {
            Result.success(getById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all pictures as a Flow of domain models.
     * Useful for observing real-time updates.
     */
    fun getAll(): Flow<List<Picture>> {
        return pictureDAO.getAll().map { list -> list.map { it.toModel() } }
    }

    /**
     * Get all pictures associated with a specific property as a Flow.
     * Allows observing changes to pictures of a particular property.
     */
    fun getPicturesForProperty(propertyId: Long): Flow<List<Picture>> {
        return pictureDAO.getForProperty(propertyId).map { list -> list.map { it.toModel() } }
    }
}
