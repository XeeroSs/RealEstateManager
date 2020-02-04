package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.models.PropertyModel


class PropertyDataRepository(private var propertyDao: PropertyDao) {

    // GET
    fun getProperty(propertyId: String) = propertyDao.getProperty(propertyId)

    // GET ALL
    fun getProperties() = propertyDao.getProperties()

    // CREATE
    fun createProperty(propertyModel: PropertyModel) = propertyDao.insertProperty(propertyModel)


    // UPDATE
    fun updateProperty(propertyModel: PropertyModel) {
      //  propertyDao.deleteProperty(propertyModel.propertyId)
        propertyDao.updateProperty(propertyModel)
    }

}