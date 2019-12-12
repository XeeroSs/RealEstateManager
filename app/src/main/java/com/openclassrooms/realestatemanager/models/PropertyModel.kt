package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Entity
data class PropertyModel(var surfaceProperty: Int = 0,
                         var typeProperty: String = "",
                         var addressProperty: String = "",
                         var addAddressProperty: String = "",
                         var zipCodeProperty: Int = 0,
                         var cityProperty: String = "",
                         var priceDollarProperty: Int = 0,
                         var roomsNumberProperty: Int = 0,
                         var bedroomsNumberProperty: Int = 0,
                         var bathroomsNumberProperty: Int = 0,
                         var descriptionProperty: String = "",
                         var statusProperty: Boolean = true,
                         var dateProperty: String = "",
                         var saleDateProperty: String = "Not sold",
                         var realEstateAgentProperty: String = "",
                         @PrimaryKey var propertyId: String = "",
                         var photosPropertyJSON: String = "")