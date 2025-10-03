package com.julien.mouellic.realestatemanager.utils

import android.content.Context

/**
 * ResponsiveUtils
 *
 * Utility object for determining screen type (phone/tablet).
 * Can be used to adjust UI layouts for different screen sizes.
 */
object ResponsiveUtils {

    /**
     * Check if the device is a tablet
     *
     * Uses the smallestScreenWidthDp to determine if the screen is wide enough
     * to be considered a tablet. The threshold is usually 600dp.
     *
     * @param context Context to access resources
     * @return true if device is a tablet, false otherwise
     */
    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.smallestScreenWidthDp >= 600
    }
}
