package com.openclassrooms.realestatemanager

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class GoogleMapTest {

    private val bredaLocationLatlng = LatLng(51.5891347, 4.7739302)
    private val bredaAddress = "Havermarkt 7-1, 4811 WD Breda, Pays-Bas"

    private fun getLatLngFromAddress(context: Context): LatLng? {
        val coder = Geocoder(context)
        val listAddress: List<Address>
        var point: LatLng? = null

        try {
            listAddress = coder.getFromLocationName(bredaAddress, 5)
            listAddress?.let {
                point = LatLng(it[0].latitude, it[0].longitude)
            } ?: return null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return point
    }

    @Test
    @Throws(Exception::class)
    fun startTest() {
        val searchBredaLatLng = getLatLngFromAddress(InstrumentationRegistry.getInstrumentation().context)
        Assert.assertEquals(searchBredaLatLng!!.latitude, bredaLocationLatlng.latitude, 0.0000001)
        Assert.assertEquals(searchBredaLatLng.longitude, bredaLocationLatlng.longitude, 0.0000001)
    }

}