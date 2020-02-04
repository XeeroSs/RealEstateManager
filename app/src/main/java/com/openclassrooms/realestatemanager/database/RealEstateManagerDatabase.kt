package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.database.dao.PropertyDao


@Database(entities = [PropertyModel::class], version = 1, exportSchema = false)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    companion object {

        // Singleton
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null

        // Instance
        fun getInstance(context: Context): RealEstateManagerDatabase? {
            INSTANCE?.let { return it } ?: synchronized(RealEstateManagerDatabase::class.java) {
                INSTANCE?.let { return it }
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RealEstateManagerDatabase::class.java, "Property.db")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE
        }
    }
}
