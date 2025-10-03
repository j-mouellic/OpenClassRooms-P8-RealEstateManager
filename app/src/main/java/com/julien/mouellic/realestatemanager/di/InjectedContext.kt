package com.julien.mouellic.realestatemanager.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * InjectedContext wraps the Android Context to provide utility functions.
 *
 * Purpose:
 * 1. **Separation of Concerns (SOC)**: Centralizes context-related operations like checking
 *    internet connectivity, avoiding scattering such code throughout the app.
 * 2. **Dependency Injection (DI)**: This class can be provided via Hilt or manually to
 *    any class that needs context-related utilities, instead of passing raw Context everywhere.
 *
 * @param _context The Android application or activity context.
 */
class InjectedContext(private val _context: Context) {

    // ----------------------------
    // Check if the device is connected to any network (Wi-Fi, Cellular, Ethernet)
    // Returns true if at least one type of network is available and active
    // ----------------------------
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get the currently active network
        val network = connectivityManager.activeNetwork ?: return false

        // Get network capabilities for that network
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        // Check for common transport types: Wi-Fi, Cellular, Ethernet
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    // ----------------------------
    // Return the wrapped Context instance
    // ----------------------------
    fun getContext(): Context {
        return _context
    }
}
