package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.PropertyWithDetailsDAO
import com.julien.mouellic.realestatemanager.domain.model.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PropertyWithDetailsRepository @Inject constructor(private val fullPropertyDAO: PropertyWithDetailsDAO) {

    /** SELECT REAL TIME / FLOW **/
    fun getByIdRT(id: Long): Flow<Property?> {
        return fullPropertyDAO.getByIdRT(id).map { it?.toModel() }
    }

    fun getAllRT(): Flow<List<Property>> {
        return fullPropertyDAO.getAllRT().map { list -> list.map { it.toModel() } }
    }

    /** SELECT UNIQUE FROM FLOW **/
    @WorkerThread
    suspend fun getByIdU(id: Long): Property? {
        return fullPropertyDAO.getByIdRT(id).first()?.toModel()
    }

    @WorkerThread
    suspend fun getAllU(): List<Property> {
        return fullPropertyDAO.getAllRT().first().map { it.toModel() }
    }

    @WorkerThread
    suspend fun getByIdUAsResult(id: Long): Result<Property?> {
        return try {
            Result.success(fullPropertyDAO.getByIdRT(id).first()?.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun getAllUAsResult(): Result<List<Property>> {
        return try {
            Result.success(fullPropertyDAO.getAllRT().first().map { it.toModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** SELECT UNIQUE SUSPENDED **/
    @WorkerThread
    suspend fun getById(id: Long): Property? {
        return fullPropertyDAO.getById(id)?.toModel()
    }

    @WorkerThread
    suspend fun getAll(): List<Property> {
        return fullPropertyDAO.getAll().map { it.toModel() }
    }

    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Property?> {
        return try {
            Result.success(fullPropertyDAO.getById(id)?.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun getAllAsResult(): Result<List<Property>> {
        return try {
            Result.success(fullPropertyDAO.getAll().map { it.toModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}