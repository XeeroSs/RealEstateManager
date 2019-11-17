package com.openclassrooms.realestatemanager.controller.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.openclassrooms.realestatemanager.adapter.PropertyRecyclerView
import com.openclassrooms.realestatemanager.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.OnItemClickListener
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.addOnItemClickListener


class MainActivity : BaseActivity() {

    lateinit var adapter: PropertyRecyclerView
    var propertiesList = ArrayList<PropertyModel>()
    private lateinit var mainViewModel: MainViewModel

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureUI()
        configureViewModel()
        getProperties()

        recyclerViewMain.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                launchPropertyDetails(position)
            }
        })
    }

    private fun launchPropertyDetails(position: Int) {
        if (propertiesList.size > position) {
            var intent = Intent(this, PropertyDetailsActivity::class.java)
            intent.putExtra(PROPERTY_ID, propertiesList[position].propertyId)
            startActivity(intent)
        }
    }

    private fun configureUI() {
        // RecyclerView
        recyclerViewMain.layoutManager = LinearLayoutManager(this)
        adapter = PropertyRecyclerView(this, propertiesList)
        recyclerViewMain.adapter = adapter
    }

    private fun getProperties() {
        mainViewModel.getProperties().observe(this, Observer { properties ->
            if (properties != null) {
                propertiesList.clear()
                propertiesList.addAll(properties)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }

}
