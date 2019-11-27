package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.wifi.WifiManager
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception


/**
 * Created by Philippe on 21/02/2018.
 */

object Utils {

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

    // Convertit une ArrayList en objet JSON pour mieux le manipulé dans SQLite
    fun serializeArrayList(imagesList: ArrayList<LinkedHashMap<String, String>>) = Gson().toJson(imagesList)

    // Convertit un objet JSON en ArrayList depuis SQLite
    fun deserializeArrayList(stringJSON: String): ArrayList<LinkedHashMap<String, String>> {
        val type = object : TypeToken<ArrayList<LinkedHashMap<String, String>>>() {}.type
        return Gson().fromJson(stringJSON, type)
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
            return dateFormat.format(Date())
        }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    fun convertDollarToEuro(dollars: Int): Int {
        return Math.round(dollars * 0.812).toInt()
    }

    fun convertEuroToDollar(euros: Int): Int {
        return Math.round(euros / 0.812).toInt()
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }
}
