package com.openclassrooms.realestatemanager.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.controller.activity.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository
import java.lang.IllegalArgumentException
import java.util.concurrent.Executor
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class ViewModelFactory(private var propertyDataRepository: PropertyDataRepository,
                       private var executor: Executor) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(propertyDataRepository, executor) as T
        }
        return throw IllegalArgumentException("Unknown ViewModel class")
    }
}