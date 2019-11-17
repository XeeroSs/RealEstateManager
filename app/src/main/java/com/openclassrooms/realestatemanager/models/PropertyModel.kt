package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList
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
                         var realEstateAgentProperty: String,
                         @PrimaryKey var propertyId: String = "",
                         var photosPropertyJSON: String = "") {
    constructor() : this(0,
            "",
            "",
            0,
            0,
            0,
            0,
            "",
            false,
            "",
            "",
            "")
}