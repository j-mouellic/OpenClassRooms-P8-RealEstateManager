package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.PictureDAO
import com.julien.mouellic.realestatemanager.domain.model.Picture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PictureRepository @Inject constructor(private val pictureDAO: PictureDAO) {

    /** INSERT **/
    @WorkerThread
    suspend fun insert(picture: Picture, propertyId : Long): Long {
        return pictureDAO.insert(picture.toDTO(propertyId))
    }

    @WorkerThread
    suspend fun insertAsResult(picture: Picture, propertyId : Long): Result<Long> {
        return try {
            Result.success(insert(picture, propertyId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(picture: Picture, propertyId : Long) {
        pictureDAO.update(picture.toDTO(propertyId))
    }

    @WorkerThread
    suspend fun updateAsResult(picture: Picture, propertyId : Long): Result<Unit> {
        return try {
            update(picture, propertyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/
    @WorkerThread
    suspend fun delete(picture: Picture, propertyId :Long) {
        pictureDAO.delete(picture.toDTO(propertyId))
    }

    @WorkerThread
    suspend fun deleteAsResult(picture: Picture, propertyId :Long): Result<Unit> {
        return try {
            delete(picture, propertyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET BY ID **/
    @WorkerThread
    suspend fun getById(id: Long): Picture? {
        return pictureDAO.getById(id)?.toModel()
    }

    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Picture?> {
        return try {
            Result.success(getById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET ALL (as Flow) **/
    fun getAll(): Flow<List<Picture>> {
        return pictureDAO.getAll().map { list -> list.map { it.toModel() } }
    }


    /** GET PICTURES FOR PROPERTY (as Flow) **/
    fun getPicturesForProperty(propertyId: Long): Flow<List<Picture>> {
        return pictureDAO.getForProperty(propertyId).map { list -> list.map { it.toModel() } }
    }
}