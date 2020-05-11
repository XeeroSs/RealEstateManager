package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.database.dao.ImageDAO
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.models.PropertyModel


class ImageDataRepository(private var imageDao: ImageDAO?) {

    // GET ALL
    fun getImages(propertyId: String) = imageDao?.getImages(propertyId)

    // DELETE
    fun deleteImage(id: Int) = imageDao?.deleteImage(id)

}