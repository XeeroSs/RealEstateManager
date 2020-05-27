package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.ImageModel

@Dao
interface ImageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(item: ImageModel)

    @Query("DELETE FROM ImageModel WHERE id = :id")
    fun deleteImage(id: Int)

    @Query("SELECT * FROM ImageModel WHERE imageId = :id")
    fun getPropertyImages(id: String): LiveData<List<ImageModel>>

    @Query("DELETE FROM ImageModel WHERE imageId = :id")
    fun deletePropertyImages(id: String)
}