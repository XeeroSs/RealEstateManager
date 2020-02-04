package com.openclassrooms.realestatemanager.controller.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertySearchRecyclerView
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.*
import kotlinx.android.synthetic.main.activity_property_search.*

class PropertySearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var adapter: PropertySearchRecyclerView? = null
    private lateinit var mainViewModel: MainViewModel
    private val propertiesList = ArrayList<PropertyModel>()
    private val propertiesListFull = ArrayList<PropertyModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_search)

        // Get ViewModel For the properties data
        Utils.configureViewModel(this)?.let {
            mainViewModel = it
            configureUI()
        } ?: finish()

        setSupportActionBar(toolbar_search as Toolbar?)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        // Interact with recyclerView
        recyclerView_Search.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                launchPropertyDetailsActivity(position)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.property_search_toolbar_navigation_menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 998 && resultCode == Activity.RESULT_OK) data?.let {
            // Gets properties with parameters set by the user
            getProperties(data.getIntExtra(MIN_SURFACE, 0),
                    data.getIntExtra(MAX_SURFACE, 0),
                    data.getIntExtra(MIN_PRICE, 0),
                    data.getIntExtra(MAX_PRICE, 0),
                    data.getIntExtra(MIN_ROOM, 0),
                    data.getIntExtra(MAX_ROOM, 0),
                    data.getIntExtra(MIN_BEDROOM, 0),
                    data.getIntExtra(MAX_BEDROOM, 0),
                    data.getIntExtra(MIN_BATHROOM, 0),
                    data.getIntExtra(MAX_BATHROOM, 0),
                    when {
                        data.getStringExtra(AVAILABLE) == "Available" -> true
                        data.getStringExtra(AVAILABLE) == "Not Available" -> false
                        else -> null
                    })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_toolbar_settings -> {
                startActivityForResult(Intent(this, SettingsSearchActivity::class.java), 998)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Launch property activity details
    private fun launchPropertyDetailsActivity(position: Int) {
        if (propertiesList.size > position) {
            val intent = Intent(this, PropertyDetailsActivity::class.java)
            intent.putExtra(PROPERTY_ID, propertiesList[position].propertyId)
            startActivity(intent)
        }
    }

    // Gets properties without filter
    private fun getProperties() {
        mainViewModel.getProperties().observe(this, Observer { properties ->
            properties?.let {
                propertiesList.clear()
                propertiesList.addAll(it)
                adapter?.notifyDataSetChanged()
            }
        })
    }

    // Gets properties with filter
    private fun getProperties(minSurface: Int,
                              maxSurface: Int,
                              minPrice: Int,
                              maxPrice: Int,
                              minRoom: Int,
                              maxRoom: Int,
                              minBedroom: Int,
                              maxBedroom: Int,
                              minBathroom: Int,
                              maxBathroom: Int,
                              available: Boolean?) {
        mainViewModel.getProperties().observe(this, Observer { properties ->
            properties?.let {
                propertiesList.clear()
                for (property in it) {
                    if (property.priceDollarProperty in minPrice..maxPrice &&
                            property.roomsNumberProperty in minRoom..maxRoom &&
                            property.bedroomsNumberProperty in minBedroom..maxBedroom &&
                            property.bathroomsNumberProperty in minBathroom..maxBathroom &&
                            property.surfaceProperty in minSurface..maxSurface) {
                        available?.let { availableNotNull ->
                            if (property.statusProperty == availableNotNull) propertiesList.add(property)
                        } ?: propertiesList.add(property)
                    }
                }
                recyclerView_Search.layoutManager = LinearLayoutManager(this)
                adapter = PropertySearchRecyclerView(this, propertiesList, propertiesListFull)
                recyclerView_Search.adapter = adapter
                adapter?.filter?.filter("")
            }
        })
    }

    private fun configureUI() {
        recyclerView_Search.layoutManager = LinearLayoutManager(this)
        adapter = PropertySearchRecyclerView(this, propertiesList, propertiesListFull)
        recyclerView_Search.adapter = adapter

        getProperties()

        searchView_Search.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    // Text entered
    override fun onQueryTextChange(newText: String?): Boolean {
        adapter?.filter?.filter(newText)
        return false
    }
}
