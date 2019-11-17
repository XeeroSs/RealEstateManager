package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.PropertyModel
import kotlinx.android.synthetic.main.property_cell.view.*


class PropertyRecyclerView(val context: Context, val propertyList: List<PropertyModel>) : RecyclerView.Adapter<PropertyRecyclerView.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PropertyViewHolder(LayoutInflater.from(context).inflate(R.layout.property_cell, parent, false))

    override fun getItemCount() = propertyList.size

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.textViewNameProperty.text = propertyList[position].addressProperty
        holder.textViewPriceProperty.text = propertyList[position].priceDollarProperty.toString()
        holder.textViewTypeProperty.text = propertyList[position].typeProperty
        holder.imageViewProperty.setImageResource(R.drawable.ic_launcher_background)
    }

    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPriceProperty = itemView.textView_price_property_item!!
        val textViewNameProperty = itemView.textView_name_property_item!!
        val textViewTypeProperty = itemView.textView_type_property_item!!
        val imageViewProperty = itemView.imageView_image_property_item!!
    }

}