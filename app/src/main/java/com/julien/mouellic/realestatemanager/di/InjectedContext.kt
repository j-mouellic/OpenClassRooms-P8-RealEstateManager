package com.julien.mouellic.realestatemanager.di


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InjectedContext(private val _context: Context) {
    /**
     * Check if we're connected to some type of Internet network.
     * Doesn't necessarily mean that the connection is working!
     */
    fun isInternetAvailable(): Boolean {
        val connectivityManager = _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    fun getContext() : Context {
        return _context
    }
}
