package com.julien.mouellic.realestatemanager.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * InternetModule provides a singleton InjectedContext for network-related utilities.
 *
 * Purpose:
 * 1. **Dependency Injection (DI)**: Allows any class in the app to get an InjectedContext
 *    via Hilt instead of passing raw Context manually.
 * 2. **Separation of Concerns (SOC)**: Keeps network checking logic encapsulated inside InjectedContext.
 *
 * Provided instance:
 * - InjectedContext: wraps the application Context and provides utility methods like
 *   isInternetAvailable().
 */
@Module
@InstallIn(SingletonComponent::class)
class InternetModule {

    // ----------------------------
    // Provide a singleton InjectedContext that wraps ApplicationContext
    // This ensures the same instance is reused throughout the app and avoids memory leaks.
    // ----------------------------
    @Provides
    @Singleton
    fun provideInternetAvailabilityContext(@ApplicationContext context: Context): InjectedContext {
        return InjectedContext(context)
    }
}
