package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = PropertyModel::class,
        parentColumns = arrayOf("propertyId"), childColumns = arrayOf("imageId"),
        onDelete = ForeignKey.CASCADE)])
data class ImageModel(var imageURL: String = "",
                      @PrimaryKey var id: String = UUID.randomUUID().toString(),
                      var imageLabel: String = "",
                      var imageId: String = "")