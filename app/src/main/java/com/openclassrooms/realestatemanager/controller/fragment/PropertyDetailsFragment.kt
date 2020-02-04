package com.openclassrooms.realestatemanager.controller.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyImageRecyclerView
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.Utils


class PropertyDetailsFragment : Fragment(), OnMapReadyCallback {

    private var propertyId: String? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: PropertyImageRecyclerView
    private lateinit var map: GoogleMap
    private var mapView: MapView? = null
    private val listImage = ArrayList<String>()
    private val listText = ArrayList<String>()
    private var property: PropertyModel? = null
    private lateinit var contextThis: Context
    private var viewFragment: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.property_details_content, container, false)
        return viewFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let { contextThis = it } ?: return

        arguments?.let {
            // Get the property id
            if (it.containsKey(PROPERTY_ID)) propertyId = it.getString(PROPERTY_ID)
        } ?: return

        // Check if propertyId is null, and initialize the ViewModel
        propertyId?.let { id ->
            Utils.configureViewModel(this, contextThis)?.let {
                mainViewModel = it
                getProperty(id)
            } ?: return
        } ?: return
    }

    // Configure the Map
    private fun configureMap(property: PropertyModel, view: View) {
        mapView = view.findViewById(R.id.liteMap_property)
        with(mapView) {
            // Initialise the MapView
            this?.onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            this?.getMapAsync(this@PropertyDetailsFragment)
        }
        if (!::map.isInitialized) return
        with(map) {
            val address = Utils.getLatLngFromAddress(property.cityProperty +
                    property.zipCodeProperty.toString().toInt() +
                    property.addressProperty, contextThis)
            address?.let {
                // Move camera on the user location
                moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(it, 15f))
                addMarker(com.google.android.gms.maps.model.MarkerOptions().position(it))
            }
            // Sets the map type
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        // If map is not initialised properly
        this.map = googleMap ?: return
    }

    // Gets property with data
    private fun getProperty(propertyId: String) {
        mainViewModel.getProperty(propertyId).observe(this, Observer { property ->
            this.property = property
            configureUI()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1233 && resultCode == Activity.RESULT_OK) activity?.onBackPressed()
    }

    // Configure the user interface with property data
    private fun configureUI() {
        property?.let {
            fun textViewCapacity(textView: TextView, text: String) {
                textView.text = getString(R.string.textview_propertydetails, textView.text, text)
            }

            viewFragment?.let { view ->
                textViewCapacity(view.findViewById(R.id.textView_author_property), it.realEstateAgentProperty)
                textViewCapacity(view.findViewById(R.id.textView_address_property), "\n${it.addressProperty}," +
                        " ${it.zipCodeProperty} ${it.cityProperty}" +
                        if (it.addAddressProperty != "") ", ${it.addressProperty}." else ".")
                textViewCapacity(view.findViewById(R.id.textView_bathrooms_property), it.bathroomsNumberProperty.toString())
                textViewCapacity(view.findViewById(R.id.textView_bedrooms_property), it.bedroomsNumberProperty.toString())
                textViewCapacity(view.findViewById(R.id.textView_description_property_fragment), it.descriptionProperty)
                textViewCapacity(view.findViewById(R.id.textView_entry_sale_property), Utils.todayDate)
                textViewCapacity(view.findViewById(R.id.textView_price_property), it.priceDollarProperty.toString() + "$")
                textViewCapacity(view.findViewById(R.id.textView_rooms_property), it.roomsNumberProperty.toString())
                textViewCapacity(view.findViewById(R.id.textView_sale_date_property), it.saleDateProperty)
                textViewCapacity(view.findViewById(R.id.textView_status_property), if (it.statusProperty) getString(R.string.availability) else getString(R.string.not_available))
                textViewCapacity(view.findViewById(R.id.textView_type_property), it.typeProperty)
                textViewCapacity(view.findViewById(R.id.textView_surface_property), it.surfaceProperty.toString() + "m")

                configureMap(it, view)
                configureRecyclerView(it.photosPropertyJSON, view)
            }
        }
    }

    // Configure RecyclerView
    private fun configureRecyclerView(photosPropertyJSON: String, view: View) {
        Utils.deserializeArrayList(photosPropertyJSON)?.let {
            listImage.addAll(it.keys)
            listText.addAll(it.values)
        }
        view.findViewById<RecyclerView>(R.id.recyclerView_photos_property_fragment).layoutManager = LinearLayoutManager(contextThis, LinearLayoutManager.HORIZONTAL, false)
        adapter = PropertyImageRecyclerView(contextThis, listImage, listText, false)
        view.findViewById<RecyclerView>(R.id.recyclerView_photos_property_fragment).adapter = adapter
    }
}