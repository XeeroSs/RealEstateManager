package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.ImageModel

@Dao
interface ImageDAO {

    @Insert
    fun insertImage(item: ImageModel)

    @Query("DELETE FROM ImageModel WHERE id = :id")
    fun deleteImage(id: Int)

    @Query("SELECT * FROM ImageModel WHERE propertyId = :id")
    fun getImages(id: String): LiveData<List<ImageModel>>
}