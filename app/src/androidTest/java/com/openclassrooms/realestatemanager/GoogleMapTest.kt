package com.openclassrooms.realestatemanager

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class GoogleMapTest {

    val bredaLocationLatlng = LatLng(51.5891347, 4.7739302)
    val bredaAddress = "Havermarkt 7-1, 4811 WD Breda, Pays-Bas"


    fun getLatLngFromAddress(address: String, context: Context): LatLng? {
        val coder = Geocoder(context)
        val listAddress: List<Address>
        var point: LatLng? = null

        try {
            listAddress = coder.getFromLocationName(address, 5)
            if (listAddress == null) return null
            point = LatLng(listAddress[0].latitude, listAddress[0].longitude)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return point
    }

    @Test
    @Throws(Exception::class)
    fun startTest() {
        val searchBredaLatLng = getLatLngFromAddress(bredaAddress, InstrumentationRegistry.getInstrumentation().context)
        Assert.assertEquals(searchBredaLatLng!!.latitude, bredaLocationLatlng.latitude, 0.0000001)
        Assert.assertEquals(searchBredaLatLng.longitude, bredaLocationLatlng.longitude, 0.0000001)
    }

}