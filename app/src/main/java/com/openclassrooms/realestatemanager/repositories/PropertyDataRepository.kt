package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.models.PropertyModel


class PropertyDataRepository(var propertyDao: PropertyDao) {

    // --- GET ---
    fun getProperty(propertyId: Int) = propertyDao.getProperty(propertyId)

    // --- GET ALL ---
    fun getProperties() = propertyDao.getProperties()

    // --- CREATE ---
    fun createProperty(propertyModel: PropertyModel) = propertyDao.insertProperty(propertyModel)

    // --- UPDATE ---
    fun updateProperty(propertyModel: PropertyModel) = propertyDao.updateProperty(propertyModel)

}