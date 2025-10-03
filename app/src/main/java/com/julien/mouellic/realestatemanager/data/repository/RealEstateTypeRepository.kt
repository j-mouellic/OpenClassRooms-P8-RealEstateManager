package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.RealEstateTypeDAO
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository for managing Real Estate Types.
 *
 * Responsibilities:
 * - Handles CRUD operations for real estate types.
 * - Converts between domain models (`RealEstateType`) and DTOs (`RealEstateTypeDTO`) using mappers.
 * - Supports both synchronous one-time operations (suspend) and real-time updates via Flow.
 * - Wraps operations in Result to safely handle exceptions without crashing the app.
 */
class RealEstateTypeRepository @Inject constructor(private val realEstateTypeDAO: RealEstateTypeDAO) {

    /** ---------------- INSERT ---------------- **/
    @WorkerThread
    suspend fun insert(realEstateType: RealEstateType): Long {
        return realEstateTypeDAO.insert(realEstateType.toDTO())
    }

    /**
     * Insert a real estate type with exception handling.
     * Returns Result to avoid app crashes if the insert fails.
     */
    @WorkerThread
    suspend fun insertAsResult(realEstateType: RealEstateType): Result<Long> {
        return try {
            Result.success(insert(realEstateType))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** ---------------- UPDATE ---------------- **/
    @WorkerThread
    suspend fun update(realEstateType: RealEstateType) {
        realEstateTypeDAO.update(realEstateType.toDTO())
    }

    /**
     * Update a real estate type with exception handling.
     * Returns Result<Unit> to safely propagate errors.
     */
    @WorkerThread
    suspend fun updateAsResult(realEstateType: RealEstateType): Result<Unit> {
        return try {
            update(realEstateType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** ---------------- DELETE ---------------- **/
    @WorkerThread
    suspend fun delete(realEstateType: RealEstateType) {
        realEstateTypeDAO.delete(realEstateType.toDTO())
    }

    /**
     * Delete a real estate type with exception handling.
     * Returns Result<Unit> to avoid crashing the app if the operation fails.
     */
    @WorkerThread
    suspend fun deleteAsResult(realEstateType: RealEstateType): Result<Unit> {
        return try {
            delete(realEstateType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** ---------------- GET BY ID ---------------- **/
    @WorkerThread
    suspend fun getById(id: Long): RealEstateType? {
        return realEstateTypeDAO.getById(id)?.toModel()
    }

    /**
     * Get a real estate type by ID with exception handling.
     * Returns Result to safely handle potential DB errors.
     */
    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<RealEstateType?> {
        return try {
            Result.success(getById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** ---------------- GET ALL ---------------- **/
    /**
     * Retrieve all real estate types as a one-time list.
     */
    @WorkerThread
    suspend fun getAll(): List<RealEstateType> {
        return realEstateTypeDAO.getAll().map { it.toModel() }
    }

    /**
     * Retrieve all real estate types as a Flow for real-time updates.
     * Any change in the database will be automatically emitted.
     */
    fun getAllAsFlow(): Flow<List<RealEstateType>> {
        return realEstateTypeDAO.getAllAsFlow().map { list -> list.map { it.toModel() } }
    }
}
