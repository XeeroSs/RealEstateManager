package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.net.wifi.WifiManager
import java.text.SimpleDateFormat
import java.util.Date
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * Created by Philippe on 21/02/2018.
 */

object Utils {

    // Convertit une ArrayList en objet JSON pour mieux le manipulé dans SQLite
    fun serializeArrayList(imagesList: ArrayList<HashMap<String, String>>) = Gson().toJson(imagesList)

    // Convertit un objet JSON en ArrayList depuis SQLite
    fun deserializeArrayList(stringJSON: String): ArrayList<HashMap<String, String>> {
        val type = object : TypeToken<ArrayList<HashMap<String, String>>>() {}.type
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
