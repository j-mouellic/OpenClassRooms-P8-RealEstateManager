package com.julien.mouellic.realestatemanager.data.repository

import androidx.annotation.WorkerThread
import com.julien.mouellic.realestatemanager.data.dao.PropertyDAO
import com.julien.mouellic.realestatemanager.domain.model.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PropertyRepository @Inject constructor(private val propertyDAO: PropertyDAO) {

    /** INSERT **/
    @WorkerThread
    suspend fun insert(property: Property): Long {
        return propertyDAO.insert(property.toDTO())
    }

    @WorkerThread
    suspend fun insert(properties: List<Property>): List<Long> {
        return propertyDAO.insertAll(properties.map { it.toDTO() })
    }

    @WorkerThread
    suspend fun insertAsResult(property: Property): Result<Long> {
        return try {
            Result.success(insert(property))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun insertAsResult(properties: List<Property>): Result<List<Long>> {
        return try {
            Result.success(insert(properties))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(property: Property): Int {
        return propertyDAO.update(property.toDTO())
    }

    @WorkerThread
    suspend fun update(properties: List<Property>): Int {
        return propertyDAO.update(properties.map { it.toDTO() })
    }

    @WorkerThread
    suspend fun updateAsResult(property: Property): Result<Int> {
        return try {
            Result.success(propertyDAO.update(property.toDTO()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun updateAsResult(properties: List<Property>): Result<Int> {
        return try {
            Result.success(propertyDAO.update(properties.map { it.toDTO() }))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/
    @WorkerThread
    suspend fun deleteAll() {
        propertyDAO.deleteAll()
    }

    @WorkerThread
    suspend fun deleteById(id: Long) {
        propertyDAO.deleteById(id)
    }

    @WorkerThread
    suspend fun delete(property: Property) {
        propertyDAO.delete(property.toDTO())
    }

    @WorkerThread
    suspend fun delete(properties: List<Property>) {
        propertyDAO.delete(properties.map { it.toDTO() })
    }

    @WorkerThread
    suspend fun deleteAllAsResult(): Result<Unit> {
        return try {
            propertyDAO.deleteAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun deleteByIdAsResult(id: Long): Result<Unit> {
        return try {
            propertyDAO.deleteById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun deleteAsResult(property: Property): Result<Unit> {
        return try {
            propertyDAO.delete(property.toDTO())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun deleteAsResult(properties: List<Property>): Result<Unit> {
        return try {
            propertyDAO.delete(properties.map { it.toDTO() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** SELECT REAL TIME / FLOW **/
    fun getByIdRT(id: Long): Flow<Property?> {
        return propertyDAO.getByIdRT(id).map { it?.toModel() }
    }

    fun getAllRT(): Flow<List<Property>> {
        return propertyDAO.getAllRT().map { list -> list.map { it.toModel() } }
    }

    fun getAllNewerToOlderRT(): Flow<List<Property>> {
        return propertyDAO.getAllNewerToOlderRT().map { list -> list.map { it.toModel() } }
    }

    /** SELECT UNIQUE FROM FLOW **/
    @WorkerThread
    suspend fun getByIdU(id: Long): Property? {
        return propertyDAO.getByIdRT(id).first()?.toModel()
    }

    @WorkerThread
    suspend fun getAllU(): List<Property> {
        return propertyDAO.getAllRT().first().map { it.toModel() }
    }

    @WorkerThread
    suspend fun getByIdUAsResult(id: Long): Result<Property?> {
        return try {
            Result.success(propertyDAO.getByIdRT(id).first()?.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun getAllUAsResult(): Result<List<Property>> {
        return try {
            Result.success(propertyDAO.getAllRT().first().map { it.toModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** SELECT UNIQUE SUSPENDED **/
    @WorkerThread
    suspend fun getById(id: Long): Property? {
        return propertyDAO.getById(id)?.toModel()
    }

    @WorkerThread
    suspend fun getAll(): List<Property> {
        return propertyDAO.getAll().map { it.toModel() }
    }

    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Property?> {
        return try {
            Result.success(propertyDAO.getById(id)?.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun getAllAsResult(): Result<List<Property>> {
        return try {
            Result.success(propertyDAO.getAll().map { it.toModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** SEARCH **/
    @WorkerThread
    suspend fun search(
        type: Long?,
        minPrice: Double?,
        maxPrice: Double?,
        minSurface: Double?,
        maxSurface: Double?,
        minNbRooms: Int?,
        maxNbRooms: Int?,
        isAvailable: Boolean?,
        commodities: List<Long>?
    ): List<Property> {

        return propertyDAO
            .search(type, minPrice, maxPrice, minSurface, maxSurface, minNbRooms, maxNbRooms, isAvailable)
            .filter { property ->
                if (commodities.isNullOrEmpty()) {
                    true
                } else {
                    property.commoditiesIds.any { commodities.contains(it) }
                }
            }
            .map { it.toModel() }
    }
}