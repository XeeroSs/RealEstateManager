package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.database.dao.ImageDAO
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.models.ImageModel
import com.openclassrooms.realestatemanager.models.PropertyModel


class ImageDataRepository(private var imageDao: ImageDAO?) {

    // GET ALL
    fun getImages(propertyId: String) = imageDao?.getPropertyImages(propertyId)

    // DELETE
    fun deleteImage(id: String) = imageDao?.deletePropertyImages(id)

    // DELETE BY ID
    fun deleteImageById(id: Int) = imageDao?.deleteImage(id)

    // INSERT
    fun insertImage(image: ImageModel) = imageDao?.insertImage(image)

}