package com.openclassrooms.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyRecyclerView
import com.openclassrooms.realestatemanager.adapter.PropertySearchRecyclerView
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.OnItemClickListener
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.addOnItemClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_property_search.*
import kotlinx.android.synthetic.main.property_details_content.*


class PropertySearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var adapter: PropertySearchRecyclerView
    private lateinit var mainViewModel: MainViewModel
    private var propertiesList = ArrayList<PropertyModel>()
    private var propertiesListFull = ArrayList<PropertyModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_search)
        configureViewModel()
        configureUI()

        recyclerViewMain.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                launchPropertyDetailsActivity(position)
            }
        })
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }

    private fun launchPropertyDetailsActivity(position: Int) {
        if (propertiesList.size > position) {
            var intent = Intent(this, PropertyDetailsActivity::class.java)
            intent.putExtra(PROPERTY_ID, propertiesList[position].propertyId)
            startActivity(intent)
        }
    }

    private fun getProperties() {
        mainViewModel.getProperties(this).observe(this, Observer { properties ->
            if (properties != null) {
                propertiesList.clear()
                propertiesList.addAll(properties)
                adapter.notifyDataSetChanged()
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

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }
}
