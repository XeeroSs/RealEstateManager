package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.database.dao.PropertyDao


@Database(entities = [PropertyModel::class], version = 1, exportSchema = false)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun propertyDao(): PropertyDao

    companion object {

        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null

        // --- INSTANCE ---
        fun getInstance(context: Context): RealEstateManagerDatabase? {
            if (INSTANCE == null) {
                synchronized(RealEstateManagerDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                RealEstateManagerDatabase::class.java, "Property.db")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
