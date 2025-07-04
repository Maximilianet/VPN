package com.example.vpn

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.vpn", appContext.packageName)
    }

    @Test
    fun defaultProfileIsAccessible() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        appContext.assets.open("default.ovpn").use { stream ->
            val text = stream.bufferedReader().readText()
            assertTrue(text.isNotBlank())
        }
    }
}