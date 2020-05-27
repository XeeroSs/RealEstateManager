package com.openclassrooms.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyRecyclerView
import com.openclassrooms.realestatemanager.controller.fragment.PropertyDetailsFragment
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : AppCompatActivity() {

    private var adapter: PropertyRecyclerView? = null
    private val propertiesList = ArrayList<PropertyModel>()
    private var propertyId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarMain)

        configureUI()
        getProperties()

        // Item Click
        recyclerViewMain.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (findViewById<FrameLayout>(R.id.activity_details_content) == null) launchPropertyDetailsActivity(position)
                else launchPropertyDetailsFragment(position)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.popBackStack()
    }

    // Menu icons are inflated just as they were with actionbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_toolbar_map -> {
                startActivity(Intent(this,
                        PropertyMapActivity::class.java))
                return true
            }
            R.id.action_toolbar_add -> {
                startActivity(Intent(this,
                        PropertyManagementActivity::class.java))
                return true
            }
            R.id.action_toolbar_search -> {
                startActivity(Intent(this,
                        PropertySearchActivity::class.java))
                return true
            }
            R.id.action_toolbar_manage -> {
                if (findViewById<FrameLayout>(R.id.activity_details_content) == null || propertyId == null) {
                    Utils.showToast(this, R.string.select_property)
                    return true
                }
                val intent = Intent(this, PropertyManagementActivity::class.java)
                intent.putExtra(PROPERTY_UPDATE, propertyId)
                startActivityForResult(intent, 1233)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Change fragment for tablet
    private fun launchPropertyDetailsFragment(position: Int) {
        if (propertiesList.size > position) {
            propertyId = propertiesList[position].propertyId
            val fragment = PropertyDetailsFragment().apply {
                this.arguments = Bundle().apply { putString(PROPERTY_ID, propertiesList[position].propertyId) }
            }
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.activity_details_content, fragment)
                    .commit()
        }
    }

    // Launch property activity
    private fun launchPropertyDetailsActivity(position: Int) {
        if (propertiesList.size > position) {
            val intent = Intent(this, PropertyDetailsActivity::class.java)
            intent.putExtra(PROPERTY_ID, propertiesList[position].propertyId)
            startActivity(intent)
        }
    }

    // Configure RecyclerView
    private fun configureUI() {
        recyclerViewMain.layoutManager = LinearLayoutManager(this)
        adapter = PropertyRecyclerView(this, propertiesList)
        recyclerViewMain.adapter = adapter
    }

    // Get properties from the ViewModel
    private fun getProperties() {
        Utils.configureViewModel(this)?.let { mainViewModel ->
            mainViewModel.getProperties()?.observe(this, Observer { properties ->
                if (properties != null) {
                    propertiesList.clear()
                    propertiesList.addAll(properties)
                    adapter?.notifyDataSetChanged()
                }
            })
        }
    }
}
