package com.openclassrooms.realestatemanager.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository
import java.lang.IllegalArgumentException
import java.util.concurrent.Executor


class ViewModelFactory(private var propertyDataRepository: PropertyDataRepository,
                       private var imageDataRepository: ImageDataRepository,
                       private var executor: Executor) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(propertyDataRepository, imageDataRepository, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}