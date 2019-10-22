package com.openclassrooms.realestatemanager.controller.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.openclassrooms.realestatemanager.PropertyRecyclerView
import com.openclassrooms.realestatemanager.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controller.activity.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.OnItemClickListener
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.addOnItemClickListener
import java.util.*


class MainActivity : BaseActivity() {

    lateinit var adapter: PropertyRecyclerView
    var propertiesList = ArrayList<PropertyModel>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var context: Context

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureUI()
        configureViewModel()
        getProperties()

        recyclerViewMain.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                var intent = Intent(context, PropertyActivity::class.java)
                intent.putExtra(PROPERTY_ID, propertiesList[position].propertyId)
                startActivity(intent)
            }
        })
    }

    private fun configureUI() {
        // RecyclerView
        recyclerViewMain.layoutManager = LinearLayoutManager(this)
        adapter = PropertyRecyclerView(this, propertiesList)
        recyclerViewMain.adapter = adapter
        context = this

        // Button
        test.setOnClickListener {
            createProperty(PropertyModel(surfaceProperty = 300,
                    typeProperty = "Duplex",
                    dateProperty = Calendar.getInstance().getTime().toString(),
                    addressProperty = "New York",
                    priceDollarProperty = 6000000,
                    saleDateProperty = "Not sold",
                    realEstateAgentProperty = "Masini",
                    descriptionProperty = "Qkef ekof zefkef zefkzef zefkzef zefkzef zefkef sdfksdf qsfkqsf qsfkqsf qsfkqsf qsfkqsf qsfkqsfqsnjfqzj fq fjkqz fjkqs fjkq qfjksqsdj fkqd fhkqd hkqd jkfq sjk ",
                    roomsNumberProperty = 6,
                    bedroomsNumberProperty = 2,
                    bathroomsNumberProperty = 1))
        }
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

    private fun createProperty(propertyModel: PropertyModel) = mainViewModel.createProperty(propertyModel)

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }

}
