package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.PropertyModel
import kotlinx.android.synthetic.main.property_cell.view.*
import java.util.*
import kotlin.collections.ArrayList

class PropertySearchRecyclerView(val context: Context,
                                 val propertyListFull: ArrayList<PropertyModel>,
                                 val propertyList: ArrayList<PropertyModel>) : RecyclerView.Adapter<PropertySearchRecyclerView.PropertySearchViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PropertySearchViewHolder(LayoutInflater.from(context).inflate(R.layout.property_cell, parent, false))

    override fun getItemCount() = propertyList.size

    override fun onBindViewHolder(holder: PropertySearchViewHolder, position: Int) {
        holder.textViewNameProperty.text = propertyList[position].addressProperty
        holder.textViewPriceProperty.text = propertyList[position].priceDollarProperty.toString()
        holder.textViewTypeProperty.text = propertyList[position].typeProperty
        Glide.with(context).load(Uri.parse(propertyList[position].photosProperty)).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imageViewProperty)
    }

    //
    override fun getFilter() = propertyListFilter

    private val propertyListFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<PropertyModel> = ArrayList()
            if (constraint.isEmpty()) filteredList.addAll(propertyListFull) else {
                val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim { it <= ' ' }
                for (item in propertyListFull)
                    if (item.addressProperty.toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            item.descriptionProperty.toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            item.cityProperty.toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            item.addAddressProperty.toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            item.realEstateAgentProperty.toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            item.zipCodeProperty.toString().toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            item.typeProperty.toLowerCase(Locale.getDefault()).contains(filterPattern)) filteredList.add(item)
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            propertyList.clear()
            propertyList.addAll(results.values as List<PropertyModel>)
            notifyDataSetChanged()
        }
    }


    class PropertySearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPriceProperty = itemView.textView_price_property_item!!
        val textViewNameProperty = itemView.textView_name_property_item!!
        val textViewTypeProperty = itemView.textView_type_property_item!!
        val imageViewProperty = itemView.imageView_image_property_item!!
    }

}
