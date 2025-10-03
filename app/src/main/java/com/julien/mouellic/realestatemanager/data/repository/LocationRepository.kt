package com.julien.mouellic.realestatemanager.data.repository

import android.Manifest
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.julien.mouellic.realestatemanager.data.dao.LocationDAO
import com.julien.mouellic.realestatemanager.domain.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDAO: LocationDAO,
) {

    /** INSERT **/
    @WorkerThread
    suspend fun insert(location: Location): Long {
        return locationDAO.insert(location.toDTO())
    }

    @WorkerThread
    suspend fun insertAsResult(location: Location): Result<Long> {
        return try {
            Result.success(insert(location))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** UPDATE **/
    @WorkerThread
    suspend fun update(location: Location) {
        locationDAO.update(location.toDTO())
    }

    @WorkerThread
    suspend fun updateAsResult(location: Location): Result<Unit> {
        return try {
            update(location)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** DELETE **/
    @WorkerThread
    suspend fun delete(location: Location) {
        locationDAO.delete(location.toDTO())
    }

    @WorkerThread
    suspend fun deleteAsResult(location: Location): Result<Unit> {
        return try {
            delete(location)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET BY ID **/
    @WorkerThread
    suspend fun getById(id: Long): Location? {
        return locationDAO.getById(id)?.toModel()
    }

    @WorkerThread
    suspend fun getByIdAsResult(id: Long): Result<Location?> {
        return try {
            Result.success(getById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET ALL **/
    @WorkerThread
    suspend fun getAll(): List<Location> {
        return locationDAO.getAll().map { it.toModel() }
    }

    @WorkerThread
    suspend fun getAllAsResult(): Result<List<Location>> {
        return try {
            Result.success(getAll())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

