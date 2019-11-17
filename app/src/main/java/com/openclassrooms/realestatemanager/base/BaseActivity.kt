package com.openclassrooms.realestatemanager.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.toolbar.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controller.activity.PropertyManagementActivity

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        setSupportActionBar(toolbarMain)
        // Show the Up button in the action bar.
        if (getLayoutId() != R.layout.activity_main && getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        }
    }

    // Menu icons are inflated just as they were with actionbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_toolbar_management -> {
                startActivity(Intent(this,
                        PropertyManagementActivity::class.java))
                return true
            }
            R.id.action_toolbar_add -> {
                startActivity(Intent(this,
                        PropertyManagementActivity::class.java))
                return true
            }
            R.id.action_toolbar_search -> {
                startActivity(Intent(this,
                        PropertyManagementActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}