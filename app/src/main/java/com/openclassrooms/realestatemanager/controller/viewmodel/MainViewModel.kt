package com.openclassrooms.realestatemanager.controller.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository
import com.openclassrooms.realestatemanager.utils.PROPERTY_COLLECTION
import java.util.concurrent.Executor

class MainViewModel(private var propertyDataRepository: PropertyDataRepository,
                    private var imageDataRepository: ImageDataRepository,
                    private var executor: Executor) : ViewModel() {

    fun getImages(propertyId: String) = imageDataRepository.getImages(propertyId)

    fun deleteImage(id: Int) = imageDataRepository.deleteImage(id)

    // Instance firebase database
    private val databaseInstance =
            FirebaseFirestore.getInstance().collection(PROPERTY_COLLECTION)

    // GETS PROPERTY
    fun getProperty(propertyId: String) = propertyDataRepository.getProperty(propertyId)

    // Gets a random property Id
    fun getPropertyId(): String? = databaseInstance.document().id

    // GETS PROPERTIES
    fun getProperties(): LiveData<List<PropertyModel>>? {
        databaseInstance.get().addOnCompleteListener { task ->
            task.result?.let { querySnapshot ->
                // Properties in the firebase database are registered in the user internal storage
                querySnapshot.documents.forEach { document ->
                    document.toObject(PropertyModel::class.java)?.let { property ->
                        propertyDataRepository.updateProperty(property)
                    }
                }
            }
        }
        // Gets property list of the user internal storage
        return propertyDataRepository.getProperties()
    }

    // CREATE PROPERTY
    fun createProperty(propertyModel: PropertyModel, context: Context, propertyId: String) = executor.execute {
        // Sets property Id
        propertyModel.propertyId = propertyId
        // Save property in firebase database
        databaseInstance.document(propertyId).set(propertyModel).addOnCompleteListener {
            executor.execute {
                // Save property in user internal storage
                propertyDataRepository.createProperty(propertyModel)
            }
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show()
        }
    }

    // UPDATE PROPERTY
    fun updateProperty(id: String, propertyModel: PropertyModel, context: Context) = executor.execute {
        // Sets property Id
        propertyModel.propertyId = id
        // Replace property in firebase database
        databaseInstance.document(id).set(propertyModel).addOnCompleteListener {
            executor.execute {
                // Replace property in user internal storage
                propertyDataRepository.updateProperty(propertyModel)
            }
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show()
        }
    }

}