package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.RealEstateTypeDAO
import com.julien.mouellic.realestatemanager.domain.model.RealEstateType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RealEstateTypeRepository @Inject constructor(private val realEstateTypeDAO: RealEstateTypeDAO) {

    /** INSERT **/
    @WorkerThread
    suspend fun insert(realEstateType: RealEstateType): Long {
        return realEstateTypeDAO.insert(realEstateType.toDTO())
    }

    @WorkerThread
    suspend fun insertAsResult(realEstateType: RealEstateType): Result<Long> {
        return try {
            Result.success(insert(realEstateType))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(realEstateType: RealEstateType) {
        realEstateTypeDAO.update(realEstateType.toDTO())
    }

    @WorkerThread
    suspend fun updateAsResult(realEstateType: RealEstateType): Result<Unit> {
        return try {
            update(realEstateType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/
    @WorkerThread
    suspend fun delete(realEstateType: RealEstateType) {
        realEstateTypeDAO.delete(realEstateType.toDTO())
    }

    @WorkerThread
    suspend fun deleteAsResult(realEstateType: RealEstateType): Result<Unit> {
        return try {
            delete(realEstateType)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET BY ID **/
    @WorkerThread
    suspend fun getById(id: Long): RealEstateType? {
        return realEstateTypeDAO.getRealEstateTypeById(id)?.toModel()
    }

    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<RealEstateType?> {
        return try {
            Result.success(getById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET ALL (as Flow) **/
    fun getAll(): Flow<List<RealEstateType>> {
        return realEstateTypeDAO.getAllRealEstateTypes().map { list -> list.map { it.toModel() } }
    }
}