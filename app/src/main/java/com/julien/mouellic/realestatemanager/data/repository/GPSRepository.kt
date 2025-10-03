package com.julien.mouellic.realestatemanager.data.repository

import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.julien.mouellic.realestatemanager.RealEstateManagerApp.AppSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Repository to manage GPS location retrieval.
 *
 * Responsibilities:
 * 1. Get the user's current location once (getLastLocation).
 * 2. Provide continuous location updates as a Flow (getLocationUpdate).
 *
 * This is mainly used to:
 * - Indicate the agent's position on the map.
 * - Automatically populate latitude and longitude when creating a property.
 */
class GPSRepository() {

    private val fusedClient =
        LocationServices.getFusedLocationProviderClient(AppSingleton.applicationInstance)

    companion object {
        private const val LOCATION_REQUEST_INTERVAL_MS: Long = 10_000 // 10 seconds
        private const val SMALLEST_DISPLACEMENT_METER = 20f // minimum distance for updates
    }

    /**
     * Provides continuous location updates as a Flow.
     * Each new location is emitted to observers automatically.
     *
     * - Uses high accuracy priority.
     * - Updates every LOCATION_REQUEST_INTERVAL_MS milliseconds.
     * - Only emits updates when the user has moved at least SMALLEST_DISPLACEMENT_METER meters.
     *
     * Important: Requires either ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission.
     */
    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun getLocationUpdate(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_REQUEST_INTERVAL_MS)
            .setMinUpdateDistanceMeters(SMALLEST_DISPLACEMENT_METER)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let { trySend(it) }
            }
        }

        // Start receiving location updates
        fusedClient.requestLocationUpdates(locationRequest, locationCallback, null)

        // Cleanup when Flow collector is cancelled
        awaitClose {
            fusedClient.removeLocationUpdates(locationCallback)
        }
    }

    /**
     * Returns the last known location of the device, if available.
     *
     * - Runs in a background thread using Dispatchers.IO.
     * - Returns null if no last known location is available.
     *
     * Important: Requires either ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission.
     */
    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    suspend fun getLastLocation(): Location? {
        return withContext(Dispatchers.IO) {
            fusedClient.lastLocation.await()
        }
    }
}
