package com.example.ecocyclesolution

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented Test Class
 * Runs on Android Emulator or Physical Device
 */

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    /**
     * Test 1:
     * Verify App Context Package Name
     */
    @Test
    fun useAppContext() {

        val appContext: Context =
            InstrumentationRegistry.getInstrumentation().targetContext

        assertEquals(
            "com.example.ecocyclesolution",
            appContext.packageName
        )
    }

    /**
     * Test 2:
     * Verify Context is Available
     */
    @Test
    fun contextIsNotNull() {

        val context =
            InstrumentationRegistry.getInstrumentation().targetContext

        assertNotNull(context)
    }

    /**
     * Test 3:
     * Verify Application Name Exists
     */
    @Test
    fun checkApplicationName() {

        val context =
            InstrumentationRegistry.getInstrumentation().targetContext

        val appName =
            context.applicationInfo.loadLabel(context.packageManager)

        assertNotNull(appName)
    }
}