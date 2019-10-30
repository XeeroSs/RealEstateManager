package com.openclassrooms.realestatemanager.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*
import kotlin.collections.HashMap

@Entity
data class PropertyModel(var surfaceProperty: Int,
                         var typeProperty: String,
                         var addressProperty: String,
                         var priceDollarProperty: Int,
                         var roomsNumberProperty: Int,
                         var bedroomsNumberProperty: Int,
                         var bathroomsNumberProperty: Int,
                         var descriptionProperty: String,
                         var statusProperty: Boolean = false,
                         var dateProperty: String,
                         var saleDateProperty: String = "Not sold",
                         var realEstateAgentProperty: String
        /*var photosProperty: HashMap<String, String>? = null*/) {
    @PrimaryKey(autoGenerate = true)
    var propertyId: Int? = null
}