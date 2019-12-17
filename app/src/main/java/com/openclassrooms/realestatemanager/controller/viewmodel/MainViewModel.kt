package com.openclassrooms.realestatemanager.controller.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository
import com.openclassrooms.realestatemanager.utils.PROPERTY_COLLECTION
import java.util.concurrent.Executor

class MainViewModel(var propertyDataRepository: PropertyDataRepository,
                    var executor: Executor) : ViewModel() {

    private var listPropertiesLiveData = MutableLiveData<List<PropertyModel>>()
    private var listProperties = ArrayList<PropertyModel>()
    private var property = MutableLiveData<PropertyModel>()
    private val databaseInstance =
            FirebaseFirestore.getInstance().collection(PROPERTY_COLLECTION)

    fun getProperty(propertyId: String): LiveData<PropertyModel> {
        return propertyDataRepository.getProperty(propertyId)
    }

    fun getProperties(): LiveData<List<PropertyModel>> {
        databaseInstance.get().addOnCompleteListener { task ->
            for (document: DocumentSnapshot in task.result!!.documents) {
                propertyDataRepository.createProperty(document.toObject(PropertyModel::class.java)!!,
                        document.id)
            }
        }
        return propertyDataRepository.getProperties()
    }

    fun createProperty(propertyModel: PropertyModel, context: Context) = executor.execute {
        databaseInstance.add(propertyModel).addOnCompleteListener {
            executor.execute {
                val id = it.result!!.id
                propertyDataRepository.createProperty(propertyModel, id)
            }
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show()
        }
    }

    fun updateProperty(id: String, propertyModel: PropertyModel, context: Context) = executor.execute {
        databaseInstance.document(id).set(propertyModel).addOnCompleteListener {
            executor.execute {
                propertyDataRepository.updateProperty(propertyModel)
            }
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show()
        }
    }

}