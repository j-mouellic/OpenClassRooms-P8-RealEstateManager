package com.julien.mouellic.realestatemanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

import com.jakewharton.threetenabp.AndroidThreeTen

@HiltAndroidApp
class RealEstateManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        AppSingleton.applicationInstance=this
    }

    object AppSingleton {
        lateinit var applicationInstance: Application
    }
}