package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.openclassrooms.realestatemanager.database.dao.ImageDAO
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.models.ImageModel


@Database(entities = [PropertyModel::class, ImageModel::class], version = 1, exportSchema = false)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun imageDao(): ImageDAO

    companion object {

        // Singleton
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null

        // Instance
        fun getInstance(context: Context): RealEstateManagerDatabase? {
            INSTANCE?.let { return it } ?: synchronized(RealEstateManagerDatabase::class.java) {
                INSTANCE?.let { return it }
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RealEstateManagerDatabase::class.java, "PropertiesAll.db")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE
        }
    }
}
