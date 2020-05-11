package com.openclassrooms.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.injection.ViewModelFactory
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.roundToInt


/**
 * Created by Philippe on 21/02/2018.
 */

object Utils {

    private fun provideImageDataSource(context: Context) =
            ImageDataRepository(RealEstateManagerDatabase.getInstance(context)?.imageDao())

    private fun providePropertyDataSource(context: Context) =
            PropertyDataRepository(RealEstateManagerDatabase.getInstance(context)?.propertyDao())

    // Provide instance database
    private fun provideViewModelFactory(context: Context) =
            ViewModelFactory(providePropertyDataSource(context),
                    provideImageDataSource(context), Executors.newSingleThreadExecutor())

    // ViewModel for Activity
    fun configureViewModel(context: FragmentActivity): MainViewModel? {
        val viewModelProvider = provideViewModelFactory(context)
        return ViewModelProviders.of(context, viewModelProvider).get(MainViewModel::class.java)
    }

    // ViewModel for Fragment
    fun configureViewModel(fragment: Fragment, context: Context): MainViewModel? {
        val viewModelProvider = provideViewModelFactory(context)
        return ViewModelProviders.of(fragment, viewModelProvider).get(MainViewModel::class.java)
    }

    // Show Toast
    fun showToast(context: Context, textId: Int) =
            Toast.makeText(context, context.getString(textId), Toast.LENGTH_SHORT).show()

    // Get LatLng from address
    fun getLatLngFromAddress(address: String, context: Context): LatLng? {
        val coder = Geocoder(context)
        val listAddress: List<Address>
        var point: LatLng? = null

        try {
            listAddress = coder.getFromLocationName(address, 5)
            listAddress?.let {
                point = LatLng(it[0].latitude, it[0].longitude)
            } ?: return null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return point
    }

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


    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val network =
                    connectivityManager.getNetworkCapabilities(networkCapabilities)
                            ?: return false
            return when {
                network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                network.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }

        return false
    }
}
