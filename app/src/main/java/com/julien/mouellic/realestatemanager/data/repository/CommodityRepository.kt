package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.CommodityDAO
import com.julien.mouellic.realestatemanager.domain.model.Commodity
import javax.inject.Inject

class CommodityRepository @Inject constructor(private val commodityDAO: CommodityDAO) {

    /** INSERT **/
    @WorkerThread
    suspend fun insert(commodity: Commodity): Long {
        return commodityDAO.insert(commodity.toDTO())
    }

    @WorkerThread
    suspend fun insertAsResult(commodity: Commodity): Result<Long> {
        return try {
            Result.success(insert(commodity))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(commodity: Commodity) {
        commodityDAO.update(commodity.toDTO())
    }

    @WorkerThread
    suspend fun updateAsResult(commodity: Commodity): Result<Unit> {
        return try {
            update(commodity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/
    @WorkerThread
    suspend fun delete(commodity: Commodity) {
        commodityDAO.delete(commodity.toDTO())
    }

    @WorkerThread
    suspend fun deleteAsResult(commodity: Commodity): Result<Unit> {
        return try {
            delete(commodity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET BY ID **/
    @WorkerThread
    suspend fun getById(id: Long): Commodity? {
        return commodityDAO.getById(id)?.toModel()
    }

    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Commodity?> {
        return try {
            Result.success(getById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET ALL **/
    @WorkerThread
    suspend fun getAll(): List<Commodity> {
        return commodityDAO.getAll().map { it.toModel() }
    }

    @WorkerThread
    suspend fun getAllAsResult(): Result<List<Commodity>> {
        return try {
            Result.success(getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}