package com.openclassrooms.realestatemanager.controller.viewmodel

import android.content.Context
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

    /* fun getProperties(): LiveData<List<PropertyModel>> {
         databaseInstance.get().addOnCompleteListener { task ->
             for (document: DocumentSnapshot in task.result!!) {
                 listProperties.add(document.toObject(PropertyModel::class.java)!!)
             }
             listPropertiesLiveData.postValue(listProperties)
         }.addOnFailureListener {
             listPropertiesLiveData = propertyDataRepository.getProperties() as MutableLiveData<List<PropertyModel>>
         }
         return propertyDataRepository.getProperties()
     }*/

    fun getProperties(context: Context): LiveData<List<PropertyModel>> {

        val listPropertiesLiveData = propertyDataRepository.getProperties()
        if (listPropertiesLiveData.value != null) {
            for (property in listPropertiesLiveData.value!!.iterator())
                createProperty(property, context)
        }

        databaseInstance.get().addOnCompleteListener { task ->
            for (document: DocumentSnapshot in task.result!!)
                listProperties.add(document.toObject(PropertyModel::class.java)!!)
            this.listPropertiesLiveData.postValue(listProperties)
        }
        return if (this.listPropertiesLiveData.value != null) this.listPropertiesLiveData else listPropertiesLiveData
    }

    fun createProperty(propertyModel: PropertyModel, context: Context) = executor.execute {
        databaseInstance.add(propertyModel).addOnCompleteListener {
            executor.execute {
                propertyDataRepository.createProperty(propertyModel, it.result!!.id)
            }
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show()
        }
    }

    fun updateProperty(id: String, propertyModel: PropertyModel, context: Context) = executor.execute {
        databaseInstance.document(id).set(propertyModel).addOnCompleteListener {
            propertyDataRepository.updateProperty(propertyModel)
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.message_error), Toast.LENGTH_SHORT).show()
        }
    }

}