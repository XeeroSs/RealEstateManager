package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = PropertyModel::class,
        parentColumns = arrayOf("propertyId"), childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE)])
data class ImageModel(var imageURL: String = "",
                      @PrimaryKey(autoGenerate = true) var id: Int = 0,
                      var imageLabel: String = "",
                      var propertyId: String = "")