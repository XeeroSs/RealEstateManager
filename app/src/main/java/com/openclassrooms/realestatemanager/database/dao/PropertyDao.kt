package com.openclassrooms.realestatemanager.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Update
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.openclassrooms.realestatemanager.models.PropertyModel


@Dao
interface PropertyDao {

    @Query("SELECT * FROM PropertyModel WHERE propertyId = :id")
    fun getProperty(id: Int): LiveData<PropertyModel>

    @Query("SELECT * FROM PropertyModel")
    fun getProperties(): LiveData<List<PropertyModel>>

    @Insert
    fun insertProperty(item: PropertyModel): Long

    @Update
    fun updateProperty(item: PropertyModel): Int

}