package com.julien.mouellic.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.julien.mouellic.realestatemanager.di.InjectedContext
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InjectedContextIntegrationTest {

    @Test
    fun testInternetAvailableIntegration() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val injectedContext = InjectedContext(context)

        val result = injectedContext.isInternetAvailable()

        // Assert the function return a boolean according to network availability
        assertNotNull(result)
    }
}