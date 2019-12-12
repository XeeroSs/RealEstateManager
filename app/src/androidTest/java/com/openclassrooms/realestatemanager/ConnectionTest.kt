package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class ConnectionTest {

    @Test
    @Throws(Exception::class)
    fun startTest() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        Assert.assertEquals(Utils.isInternetAvailable(context), false)
    }
}