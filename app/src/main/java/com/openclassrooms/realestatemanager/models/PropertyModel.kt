package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.models.PropertyModel.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME, indices = [Index("propertyContentProviderId", unique = true), Index("propertyId", unique = true)], primaryKeys = ["propertyContentProviderId", "propertyId"])
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
                         @ColumnInfo(name = "propertyId")
                         var propertyId: String = "",
                         @ColumnInfo(name = "propertyContentProviderId")
                         var propertyContentProviderId: Long = 0,
                         var photosProperty: String = "") {

    companion object {
        const val TABLE_NAME: String = "properties"
    }

    fun fromContentValues(values: ContentValues): PropertyModel {
        val property = PropertyModel()
        if (values.containsKey("surfaceProperty"))
            property.surfaceProperty = values.getAsInteger("surfaceProperty")
        if (values.containsKey("typeProperty"))
            property.typeProperty = values.getAsString("typeProperty")
        if (values.containsKey("addressProperty"))
            property.addressProperty = values.getAsString("addressProperty")
        if (values.containsKey("addAddressProperty"))
            property.addAddressProperty = values.getAsString("addAddressProperty")
        if (values.containsKey("zipCodeProperty"))
            property.zipCodeProperty = values.getAsInteger("zipCodeProperty")
        if (values.containsKey("cityProperty"))
            property.cityProperty = values.getAsString("cityProperty")
        if (values.containsKey("priceDollarProperty"))
            property.priceDollarProperty = values.getAsInteger("priceDollarProperty")
        if (values.containsKey("roomsNumberProperty"))
            property.roomsNumberProperty = values.getAsInteger("roomsNumberProperty")
        if (values.containsKey("bedroomsNumberProperty"))
            property.bedroomsNumberProperty = values.getAsInteger("bedroomsNumberProperty")
        if (values.containsKey("bathroomsNumberProperty"))
            property.bathroomsNumberProperty = values.getAsInteger("bathroomsNumberProperty")
        if (values.containsKey("descriptionProperty"))
            property.descriptionProperty = values.getAsString("descriptionProperty")
        if (values.containsKey("statusProperty"))
            property.statusProperty = values.getAsBoolean("statusProperty")
        if (values.containsKey("dateProperty"))
            property.dateProperty = values.getAsString("dateProperty")
        if (values.containsKey("saleDateProperty"))
            property.saleDateProperty = values.getAsString("saleDateProperty")
        if (values.containsKey("realEstateAgentProperty"))
            property.realEstateAgentProperty = values.getAsString("realEstateAgentProperty")
        if (values.containsKey("propertyId"))
            property.propertyId = values.getAsString("propertyId")
        if (values.containsKey("photosProperty"))
            property.photosProperty = values.getAsString("photosProperty")
        if (values.containsKey("propertyContentProviderId"))
            property.propertyContentProviderId = values.getAsLong("propertyContentProviderId")
        return property
    }

}