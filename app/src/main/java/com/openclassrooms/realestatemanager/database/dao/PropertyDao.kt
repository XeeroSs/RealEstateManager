package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.PropertyModel


@Dao
interface PropertyDao {

    // Get property with Cursor
    @Query("SELECT * FROM ${PropertyModel.TABLE_NAME} WHERE propertyContentProviderId = :id")
    fun getPropertyWithCursor(id: Long): Cursor

    // Gets property
    @Query("SELECT * FROM ${PropertyModel.TABLE_NAME} WHERE propertyId = :id")
    fun getProperty(id: String): LiveData<PropertyModel>

    // Gets properties
    @Query("SELECT * FROM ${PropertyModel.TABLE_NAME}")
    fun getProperties(): LiveData<List<PropertyModel>>

    // insert property
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProperty(item: PropertyModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateProperty(item: PropertyModel): Long

    @Query("DELETE FROM ${PropertyModel.TABLE_NAME} WHERE propertyId = :id")
    fun deleteProperty(id: String)

}