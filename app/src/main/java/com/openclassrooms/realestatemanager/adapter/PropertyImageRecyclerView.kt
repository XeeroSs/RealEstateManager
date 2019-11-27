package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.image_property_cell.view.*


class PropertyImageRecyclerView(val context: Context, val imagePropertiesList: ArrayList<LinkedHashMap<String, String>>) : RecyclerView.Adapter<PropertyImageRecyclerView.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PropertyViewHolder(LayoutInflater.from(context).inflate(R.layout.image_property_cell, parent, false))

    override fun getItemCount() = imagePropertiesList.size

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.textViewImageLabelProperty.text = imagePropertiesList[position].values.toString()
        holder.ImageViewImageProperty.setImageBitmap(
                MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        Uri.parse(imagePropertiesList[position].keys.iterator().next())))
    }

    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ImageViewImageProperty = itemView.imagePropertyCell_ImageView!!
        val textViewImageLabelProperty = itemView.imagePropertyCell_textImage!!
    }

}