package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.PropertyModel
import kotlinx.android.synthetic.main.property_cell.view.*


class PropertyRecyclerView(val context: Context, private val propertyList: List<PropertyModel>) : RecyclerView.Adapter<PropertyRecyclerView.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PropertyViewHolder(LayoutInflater.from(context).inflate(R.layout.property_cell, parent, false))

    override fun getItemCount() = propertyList.size

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.textViewNameProperty.text = propertyList[position].addressProperty
        holder.textViewPriceProperty.text = propertyList[position].priceDollarProperty.toString() + "$"
        holder.textViewTypeProperty.text = propertyList[position].typeProperty
        Glide.with(context).load(Uri.parse(propertyList[position].photosProperty)).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imageViewProperty)
    }

    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPriceProperty = itemView.textView_price_property_item!!
        val textViewNameProperty = itemView.textView_name_property_item!!
        val textViewTypeProperty = itemView.textView_type_property_item!!
        val imageViewProperty = itemView.imageView_image_property_item!!
    }

}