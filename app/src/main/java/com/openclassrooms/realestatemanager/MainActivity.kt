package com.openclassrooms.realestatemanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : BaseActivity() {

    val propertyList:ArrayList<PropertyModel> = ArrayList()

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creates a vertical Layout Manager
        recyclerViewMain.layoutManager = LinearLayoutManager(this)

        // Access the RecyclerView Adapter and load the data into it
        recyclerViewMain.adapter = PropertyRecyclerView(this, propertyList)

    }
}
