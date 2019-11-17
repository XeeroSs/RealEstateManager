package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.runner.RunWith

import androidx.room.Room
import org.junit.Before
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.After
import org.junit.Rule
import junit.framework.Assert.assertTrue
import org.junit.Test

class PropertyDAOTest {

    private val PROPERTY_ID: String = "TEST"
    private val PROPERTY_DEMO = PropertyModel(surfaceProperty = 0,
            typeProperty = "",
            addressProperty = "",
            priceDollarProperty = 0,
            roomsNumberProperty = 0,
            bedroomsNumberProperty = 0,
            bathroomsNumberProperty = 0,
            descriptionProperty = "",
            dateProperty = Utils.todayDate.toString(),
            realEstateAgentProperty = "",
            propertyId = PROPERTY_ID)

    // FOR DATA
    private lateinit var database: RealEstateManagerDatabase

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
                RealEstateManagerDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndGetUser() {
        this.database.propertyDao().insertProperty(PROPERTY_DEMO)
        val property = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID))
        assertTrue(property!!.propertyId == PROPERTY_ID)
    }
}
