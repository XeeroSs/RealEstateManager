package com.openclassrooms.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyImageRecyclerView
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.PROPERTY_UPDATE
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.activity_property_management.*
import kotlinx.android.synthetic.main.property_details_content.*

class PropertyDetailsActivity : BaseActivity(), OnMapReadyCallback {

    override fun getLayoutId() = R.layout.activity_property_details
    private lateinit var propertyId: String
    private lateinit var mainViewModel: MainViewModel
    lateinit var adapter: PropertyImageRecyclerView
    private lateinit var map: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var property: PropertyModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = findViewById(R.id.liteMap_property)
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync(this@PropertyDetailsActivity)
        }
        configureViewModel()
        getPropertyId()
        setSupportActionBar((management_toolbar as Toolbar?))
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.property_details_toolbar_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_toolbar_management -> {
                val intent = Intent(this, PropertyManagementActivity::class.java)
                intent.putExtra(PROPERTY_UPDATE, property.propertyId)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureMap(property: PropertyModel) {
        if (!::map.isInitialized) return
        with(map) {
            val address = Utils.getLatLngFromAddress(property.addressProperty, this@PropertyDetailsActivity)
            moveCamera(CameraUpdateFactory.newLatLngZoom(address, 15f))
            addMarker(MarkerOptions().position(address!!))
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(this)
        // If map is not initialised properly
        this.map = googleMap ?: return
    }

    private fun getPropertyId() {
        propertyId = intent.getStringExtra(PROPERTY_ID)
        getProperty()
    }

    private fun getProperty() {
        mainViewModel.getProperty(propertyId).observe(this, Observer { property ->
            if (property == null) finish()
            this.property = property
            configureUI()
        })
    }

    private fun configureUI() {
        fun textViewCapacity(textView: TextView, text: String) {
            textView.text = "${textView.text} $text"
        }
        textViewCapacity(textView_author_property, property.realEstateAgentProperty)
        textViewCapacity(textView_address_property, "\n${property.addressProperty}")
        textViewCapacity(textView_bathrooms_property, property.bathroomsNumberProperty.toString())
        textViewCapacity(textView_bedrooms_property, property.bedroomsNumberProperty.toString())
        textViewCapacity(textView_description_property_fragment, property.descriptionProperty)
        textViewCapacity(textView_entry_sale_property, Utils.todayDate)
        textViewCapacity(textView_price_property, property.priceDollarProperty.toString() + "$")
        textViewCapacity(textView_rooms_property, property.roomsNumberProperty.toString())
        textViewCapacity(textView_sale_date_property, property.saleDateProperty)
        textViewCapacity(textView_status_property, if (property.statusProperty) getString(R.string.available) else getString(R.string.not_available))
        textViewCapacity(textView_type_property, property.typeProperty)
        textViewCapacity(textView_surface_property, property.surfaceProperty.toString() + "m")

        if (map != null) configureMap(property)
        configureRecyclerView(property.photosPropertyJSON)
    }

    private fun configureRecyclerView(photosPropertyJSON: String) {
        recyclerView_photos_property_fragment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = PropertyImageRecyclerView(this, Utils.deserializeArrayList(photosPropertyJSON))
        recyclerView_photos_property_fragment.adapter = adapter
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }
}