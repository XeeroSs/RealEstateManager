package com.openclassrooms.realestatemanager.injection

import android.content.Context
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository
import java.util.concurrent.Executors

class Injection {
    companion object {
        fun providePropertyDataSource(context: Context): PropertyDataRepository {
            val database = RealEstateManagerDatabase.getInstance(context)
            return PropertyDataRepository(database!!.propertyDao())
        }

        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val dataSourceProperty = providePropertyDataSource(context)
            return ViewModelFactory(dataSourceProperty, Executors.newSingleThreadExecutor())
        }
    }
}