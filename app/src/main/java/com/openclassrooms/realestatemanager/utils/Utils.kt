package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


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
    fun serializeArrayList(imagesList: ArrayList<LinkedHashMap<Bitmap, String>>) = Gson().toJson(imagesList)

    // Convertit un objet JSON en ArrayList depuis SQLite
    fun deserializeArrayList(stringJSON: String): ArrayList<LinkedHashMap<Bitmap, String>> {
        val type = object : TypeToken<ArrayList<LinkedHashMap<Bitmap, String>>>() {}.type
        return Gson().fromJson<ArrayList<LinkedHashMap<Bitmap, String>>>(stringJSON, type)
    }

    fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream)
        return stream.toByteArray()
    }

    fun getImage(image: ByteArray) = BitmapFactory.decodeByteArray(image, 0, image.size)

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    val todayDate: String
        get() {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param money
     * @return
     */
    fun convertDollarToEuro(dollars: Int) = (dollars * 0.812).roundToInt()


    fun convertEuroToDollar(euros: Int) = (euros / 0.812).roundToInt()


    /*fun convertMoney(money: Int, isDollar: Boolean) =
            if (isDollar) (money * 0.812).roundToInt() else
                (money / 0.812).roundToInt()*/


    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val network = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return network.activeNetworkInfo != null && network.activeNetworkInfo.isConnected
    }
}
