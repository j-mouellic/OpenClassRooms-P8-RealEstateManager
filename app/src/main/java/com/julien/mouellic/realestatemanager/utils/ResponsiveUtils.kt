package com.julien.mouellic.realestatemanager.utils

import android.content.Context

object ResponsiveUtils {
    fun isTablet(context: Context): Boolean{
        return context.resources.configuration.smallestScreenWidthDp >= 600
    }
}