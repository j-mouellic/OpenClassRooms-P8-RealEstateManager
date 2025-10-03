package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.data.repository.GPSRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * GPSModule provides the GPSRepository dependency for the app.
 *
 * Purpose of using Hilt here:
 * 1. **Separation of Concerns (SOC)**: Any class (e.g., ViewModel) that needs GPS functionality
 *    does not need to know how to instantiate GPSRepository.
 * 2. **Dependency Injection (DI)**: Hilt automatically injects the repository wherever required.
 * 3. **SingletonComponent**: The provided GPSRepository instance can be reused across the entire
 *    application lifecycle if needed.
 */
@Module
@InstallIn(SingletonComponent::class) // Makes this module available app-wide
class GPSModule {

    // ----------------------------
    // Provides a GPSRepository instance
    // Responsible for accessing device location using FusedLocationProviderClient
    // Can be injected into ViewModels to get last location or continuous updates
    // ----------------------------
    @Provides
    fun provideGPSRepository(): GPSRepository {
        return GPSRepository()
    }
}
