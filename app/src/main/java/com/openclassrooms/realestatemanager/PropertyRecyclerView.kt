package com.openclassrooms.realestatemanager

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.property_cell.view.*

class PropertyRecyclerView(val context: Context, val propertyList: ArrayList<PropertyModel>) : RecyclerView.Adapter<PropertyRecyclerView.PropertyViewHolder>() {

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            PropertyViewHolder(LayoutInflater.from(context).inflate(R.layout.property_cell, parent, false))

    override fun getItemCount() = propertyList.size

    override fun onBindViewHolder(holder: PropertyViewHolder?, position: Int) {
        holder!!.textViewPriceProperty.text = propertyList[position].priceDollarProperty
        holder.textViewNameProperty.text = propertyList[position].addressProperty
        holder.textViewTypeProperty.text = propertyList[position].typeProperty
        holder.imageViewProperty.setBackgroundResource(R.drawable.ic_launcher_background)
    }


    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPriceProperty = itemView.textView_price_property_item!!
        val textViewNameProperty = itemView.textView_name_property_item!!
        val textViewTypeProperty = itemView.textView_type_property_item!!
        val imageViewProperty = itemView.imageView_image_property_item!!
    }

}