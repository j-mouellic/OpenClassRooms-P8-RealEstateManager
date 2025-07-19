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
    suspend fun insert(picture: Picture): Long {
        return pictureDAO.insert(picture.toDTO())
    }

    @WorkerThread
    suspend fun insertAsResult(picture: Picture): Result<Long> {
        return try {
            Result.success(insert(picture))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(picture: Picture) {
        pictureDAO.update(picture.toDTO())
    }

    @WorkerThread
    suspend fun updateAsResult(picture: Picture): Result<Unit> {
        return try {
            update(picture)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/
    @WorkerThread
    suspend fun delete(picture: Picture) {
        pictureDAO.delete(picture.toDTO())
    }

    @WorkerThread
    suspend fun deleteAsResult(picture: Picture): Result<Unit> {
        return try {
            delete(picture)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET BY ID **/
    @WorkerThread
    suspend fun getById(id: Long): Picture? {
        return pictureDAO.getPictureById(id)?.toModel()
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
        return pictureDAO.getAllPictures().map { list -> list.map { it.toModel() } }
    }


    /** GET PICTURES FOR PROPERTY (as Flow) **/
    fun getPicturesForProperty(propertyId: Long): Flow<List<Picture>> {
        return pictureDAO.getPicturesForProperty(propertyId).map { list -> list.map { it.toModel() } }
    }
}