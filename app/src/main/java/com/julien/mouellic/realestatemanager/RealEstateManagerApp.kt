package com.julien.mouellic.realestatemanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * RealEstateManagerApp
 *
 * Main Application class for the app.
 * - Initializes Dagger Hilt for dependency injection (@HiltAndroidApp)
 * - Initializes ThreeTenABP for Java 8 Date/Time API support on Android
 * - Provides a global singleton reference to the Application context
 */
@HiltAndroidApp
class RealEstateManagerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize ThreeTenABP (Java 8 time support for Android)
        AndroidThreeTen.init(this)

        // Set singleton instance to access application context globally
        AppSingleton.applicationInstance = this
    }

    /**
     * AppSingleton
     *
     * Holds a global reference to the Application instance.
     * Can be used to access context anywhere in the app.
     */
    object AppSingleton {
        lateinit var applicationInstance: Application
    }
}
