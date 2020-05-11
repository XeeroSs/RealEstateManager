package com.openclassrooms.realestatemanager.adapter

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.ImageModel
import kotlinx.android.synthetic.main.image_property_cell.view.*


class PropertyImageRecyclerView(val context: Context, private val imageModel: ArrayList<ImageModel>,
                                private val isManage: Boolean) : RecyclerView.Adapter<PropertyImageRecyclerView.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PropertyViewHolder(LayoutInflater.from(context).inflate(R.layout.image_property_cell, parent, false))

    override fun getItemCount() = imageModel.size

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.textViewImageLabelProperty.text = imageModel[position].imageLabel
        Glide.with(context).load(Uri.parse(imageModel[position].imageURL)).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.imageViewImageProperty)
        if (!isManage) {
            holder.imageViewDeleteImageProperty.visibility = View.GONE
        } else {
            holder.imageViewDeleteImageProperty.setOnClickListener {
                imageModel.removeAt(position)
                notifyDataSetChanged()
            }
        }

    }

    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewImageProperty: ImageView = itemView.imagePropertyCell_ImageView
        val textViewImageLabelProperty: TextView = itemView.imagePropertyCell_textImage
        val imageViewDeleteImageProperty: ImageView = itemView.imagePropertyCell_deleteImage
    }
}
