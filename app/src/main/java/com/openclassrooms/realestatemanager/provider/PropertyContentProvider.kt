package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.models.PropertyModel.Companion.TABLE_NAME

class PropertyContentProvider : ContentProvider() {

    companion object {
        val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
        val URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    override fun onCreate(): Boolean = true

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        context?.let { context ->
            val propertyId = ContentUris.parseId(uri)
            RealEstateManagerDatabase.getInstance(context)?.propertyDao()?.let {
                val cursor = it.getPropertyWithCursor(propertyId)
                cursor.setNotificationUri(context.contentResolver, uri)
                return cursor
            }
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri) = "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        context?.let { context ->
            values?.let { contentValues ->
                val property = PropertyModel().fromContentValues(contentValues)
                RealEstateManagerDatabase.getInstance(context)?.propertyDao()?.insertProperty(property)?.let {
                    context.contentResolver.notifyChange(uri, null)
                    return ContentUris.withAppendedId(uri, it)
                }
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //...
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        context?.let { context ->
            values?.let { contentValues ->
                val property = PropertyModel().fromContentValues(contentValues)
                RealEstateManagerDatabase.getInstance(context)?.propertyDao()?.updateProperty(property)?.let {
                    context.contentResolver.notifyChange(uri, null)
                    return it.toInt()
                }
            }
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }
}