package com.openclassrooms.realestatemanager.controller.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyImageRecyclerView
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.PROPERTY_UPDATE
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.configureViewModel
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.property_details_content.*

class PropertyDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var propertyId: String? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: PropertyImageRecyclerView
    private lateinit var map: GoogleMap
    private var mapView: MapView? = null
    private val listImage = ArrayList<String>()
    private val listText = ArrayList<String>()
    private var property: PropertyModel? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1233 && resultCode == Activity.RESULT_OK) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_details)

        // Get the property id
        propertyId = intent.getStringExtra(PROPERTY_ID)

        // Check if propertyId is null, and initialize the ViewModel
        propertyId?.let { id ->
            configureViewModel(this)?.let {
                mainViewModel = it
                getProperty(id)
            } ?: finish()
        } ?: finish()

        setSupportActionBar((management_toolbar as Toolbar?))
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        // Get reference Map
        mapView = liteMap_property
        with(mapView) {
            // Initialise the MapView
            this?.onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            this?.getMapAsync(this@PropertyDetailsActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.property_details_toolbar_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_toolbar_management -> {
                property?.let {
                    val intent = Intent(this, PropertyManagementActivity::class.java)
                    intent.putExtra(PROPERTY_UPDATE, it.propertyId)
                    startActivityForResult(intent, 1233)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Configure the Map
    private fun configureMap(property: PropertyModel) {
        if (!::map.isInitialized) return
        with(map) {
            // Get LatLng of property address information
            val address = Utils.getLatLngFromAddress(property.cityProperty +
                    property.zipCodeProperty.toString().toInt() +
                    property.addressProperty, this@PropertyDetailsActivity)
            address?.let {
                // Move camera on the user location
                moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                addMarker(MarkerOptions().position(it))
            }
            // Sets the map type
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(this)
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

    // Configure the user interface with property data
    private fun configureUI() {
        property?.let {
            fun textViewCapacity(textView: TextView, text: String) {
                textView.text = getString(R.string.textview_propertydetails, textView.text, text)
            }

            textViewCapacity(textView_author_property, it.realEstateAgentProperty + " (Entry date: ${it.dateProperty})")
            textViewCapacity(textView_address_property, "\n${it.addressProperty}," +
                    " ${it.zipCodeProperty} ${it.cityProperty}" +
                    if (it.addAddressProperty != "") ", ${it.addAddressProperty}." else ".")
            textViewCapacity(textView_bathrooms_property, it.bathroomsNumberProperty.toString())
            textViewCapacity(textView_bedrooms_property, it.bedroomsNumberProperty.toString())
            textViewCapacity(textView_description_property_fragment, it.descriptionProperty)
            textViewCapacity(textView_price_property, it.priceDollarProperty.toString() + "$")
            textViewCapacity(textView_rooms_property, it.roomsNumberProperty.toString())
            textViewCapacity(textView_status_property, if (it.statusProperty) getString(R.string.availability) else getString(R.string.not_available) + " (Sale date: ${it.saleDateProperty})")
            textViewCapacity(textView_type_property, it.typeProperty)
            textViewCapacity(textView_surface_property, it.surfaceProperty.toString())

            configureMap(it)
            configureRecyclerView(it.photosPropertyJSON)
        }
    }

    // Configure RecyclerView
    private fun configureRecyclerView(photosPropertyJSON: String) {
        Utils.deserializeArrayList(photosPropertyJSON)?.let {
            listImage.addAll(it.keys)
            listText.addAll(it.values)
        }
        recyclerView_photos_property_fragment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = PropertyImageRecyclerView(this, listImage, listText, false)
        recyclerView_photos_property_fragment.adapter = adapter
    }
}