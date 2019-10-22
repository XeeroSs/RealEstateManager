package com.openclassrooms.realestatemanager.controller.activity.viewmodel

import android.arch.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository
import java.util.concurrent.Executor

class MainViewModel(var propertyDataRepository: PropertyDataRepository,
                    var executor: Executor) : ViewModel() {

    fun getProperty(propertyId: Int) = propertyDataRepository.getProperty(propertyId)

    fun getProperties() = propertyDataRepository.getProperties()

    fun createProperty(propertyModel: PropertyModel) = executor.execute {
        propertyDataRepository.createProperty(propertyModel)
    }

    fun updateProperty(propertyModel: PropertyModel) = executor.execute {
        propertyDataRepository.updateProperty(propertyModel)
    }

}