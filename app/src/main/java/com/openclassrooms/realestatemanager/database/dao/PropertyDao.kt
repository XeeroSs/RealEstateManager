package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.PropertyModel


@Dao
interface PropertyDao {

    // Gets property
    @Query("SELECT * FROM PropertyModel WHERE propertyId = :id")
    fun getProperty(id: String): LiveData<PropertyModel>

    // Gets properties
    @Query("SELECT * FROM PropertyModel")
    fun getProperties(): LiveData<List<PropertyModel>>

    // insert property
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProperty(item: PropertyModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateProperty(item: PropertyModel)

    @Query("DELETE FROM PropertyModel WHERE propertyId = :id")
    fun deleteProperty(id: String)

}