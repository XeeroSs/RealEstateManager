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

        fun provideExecutor() = Executors.newSingleThreadExecutor()

        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val dataSourceProperty = providePropertyDataSource(context)
            val executor = provideExecutor()
            return ViewModelFactory(dataSourceProperty, executor)
        }
    }
}