package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.PropertyWithDetailsDAO
import com.julien.mouellic.realestatemanager.domain.model.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository for managing full property details.
 *
 * Responsibilities:
 * - Provides access to properties along with their associated details:
 *   agent, location, real estate type, pictures, and commodities.
 * - Supports both real-time updates via Flow and one-time retrieval via suspend functions.
 * - Wraps operations in Result when needed to avoid app crashes.
 *
 * This repository is designed for views where all property details are required (e.g., SHOW screens).
 */
class PropertyWithDetailsRepository @Inject constructor(private val fullPropertyDAO: PropertyWithDetailsDAO) {

    /** ---------------- REAL TIME / FLOW ---------------- **/
    /**
     * Real-time flow of a property by ID.
     * Emits updates automatically when any related details change in the database.
     */
    fun getByIdRT(id: Long): Flow<Property?> {
        return fullPropertyDAO.getByIdRT(id).map { it?.toModel() }
    }

    /** Real-time flow of all properties with details. */
    fun getAllRT(): Flow<List<Property>> {
        return fullPropertyDAO.getAllRT().map { list -> list.map { it.toModel() } }
    }

    /** ---------------- SELECT UNIQUE FROM FLOW ---------------- **/
    /**
     * Retrieve a single property by ID from a Flow.
     * Converts the flow into a one-time value using `first()`.
     */
    @WorkerThread
    suspend fun getByIdU(id: Long): Property? {
        return fullPropertyDAO.getByIdRT(id).first()?.toModel()
    }

    /**
     * Retrieve all properties from a Flow as a one-time list.
     */
    @WorkerThread
    suspend fun getAllU(): List<Property> {
        return fullPropertyDAO.getAllRT().first().map { it.toModel() }
    }

    /**
     * Retrieve a single property by ID from a Flow, wrapped in Result to safely handle exceptions.
     */
    @WorkerThread
    suspend fun getByIdUAsResult(id: Long): Result<Property?> {
        return try {
            Result.success(fullPropertyDAO.getByIdRT(id).first()?.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieve all properties from a Flow as a one-time list, wrapped in Result.
     */
    @WorkerThread
    suspend fun getAllUAsResult(): Result<List<Property>> {
        return try {
            Result.success(fullPropertyDAO.getAllRT().first().map { it.toModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** ---------------- SELECT UNIQUE SUSPENDED ---------------- **/
    /**
     * Retrieve a single property by ID using a standard suspend function.
     * This is a one-time fetch and does not observe real-time updates.
     */
    @WorkerThread
    suspend fun getById(id: Long): Property? {
        return fullPropertyDAO.getById(id)?.toModel()
    }

    /**
     * Retrieve all properties as a one-time list using a standard suspend function.
     */
    @WorkerThread
    suspend fun getAll(): List<Property> {
        return fullPropertyDAO.getAll().map { it.toModel() }
    }

    /**
     * Retrieve a single property by ID, wrapped in Result to safely handle exceptions.
     */
    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Property?> {
        return try {
            Result.success(fullPropertyDAO.getById(id)?.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieve all properties, wrapped in Result to safely handle exceptions.
     */
    @WorkerThread
    suspend fun getAllAsResult(): Result<List<Property>> {
        return try {
            Result.success(fullPropertyDAO.getAll().map { it.toModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
