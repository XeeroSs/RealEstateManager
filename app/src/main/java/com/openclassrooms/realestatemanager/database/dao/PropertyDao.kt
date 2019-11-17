package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Update
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.PropertyModel


@Dao
interface PropertyDao {

    @Query("SELECT * FROM PropertyModel WHERE propertyId = :id")
    fun getProperty(id: String): LiveData<PropertyModel>

    @Query("SELECT * FROM PropertyModel")
    fun getProperties(): LiveData<List<PropertyModel>>

    @Insert
    fun insertProperty(item: PropertyModel)

    @Update
    fun updateProperty(item: PropertyModel)

}