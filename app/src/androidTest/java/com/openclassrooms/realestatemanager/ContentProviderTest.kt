package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.provider.PropertyContentProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before

class ContentProviderTest {

    // FOR DATA
    private lateinit var contentResolver: ContentResolver

    private val propertyId: Long = 2

    @Before
    @Throws(Exception::class)
    fun initialize() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        contentResolver = InstrumentationRegistry.getContext().contentResolver;
    }

    @org.junit.Test
    @Throws(Exception::class)
    fun getPropertyEmpty() {
        val cursor = contentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI, propertyId), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor?.count, `is`(1))
        cursor?.close()
    }

    @org.junit.Test
    @Throws(Exception::class)
    fun insertAndGetProperty() {
        // Insert property
        contentResolver.insert(PropertyContentProvider.URI, createProperty())
        // Get property
        val cursor = contentResolver.query(ContentUris.withAppendedId(PropertyContentProvider.URI, propertyId), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor?.count, `is`(1))
        assertThat(cursor?.moveToFirst(), `is`(true))
        assertThat(cursor?.getString(cursor.getColumnIndexOrThrow("surfaceProperty")), `is`("2"))
    }

    private fun createProperty(): ContentValues {
        val values = ContentValues()
        values.put("surfaceProperty", "2")
        values.put("typeProperty", "2")
        values.put("addressProperty", "2")
        values.put("addAddressProperty", "2")
        values.put("zipCodeProperty", "2")
        values.put("cityProperty", "2")
        values.put("priceDollarProperty", "2")
        values.put("roomsNumberProperty", "2")
        values.put("bedroomsNumberProperty", "2")
        values.put("bathroomsNumberProperty", "2")
        values.put("descriptionProperty", "2")
        values.put("statusProperty", "2")
        values.put("dateProperty", "2")
        values.put("saleDateProperty", "2")
        values.put("realEstateAgentProperty", "2")
        values.put("propertyId", "2")
        values.put("propertyContentProviderId", "2")
        return values
    }
}