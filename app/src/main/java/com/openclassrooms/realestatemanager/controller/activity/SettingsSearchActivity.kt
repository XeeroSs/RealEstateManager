package com.openclassrooms.realestatemanager.controller.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.*
import kotlinx.android.synthetic.main.activity_settings_search.*
import java.util.*

class SettingsSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_search)

        setSupportActionBar(toolbar_settings as Toolbar?)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        configureSpinners()
        configureSeekBar()
        configureUI()
    }

    private fun configureSpinners() {
        ArrayAdapter.createFromResource(
                this,
                R.array.available_search, android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            available_spinner_search.adapter = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureSeekBar() {
        surface_seekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textview_seekbar_surface.text = getString(R.string.category_numberstart_numberend,
                    getString(R.string.surface_in_m), minValue.toInt(),
                    maxValue.toInt())
        }

        price_seekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textview_seekbar_price.text = getString(R.string.category_numberstart_numberend,
                    getString(R.string.price_dollars), minValue.toInt(),
                    maxValue.toInt())
        }

        room_seekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textview_seekbar_room.text = getString(R.string.category_numberstart_numberend,
                    getString(R.string.number_room), minValue.toInt(),
                    maxValue.toInt())
        }

        bedroom_seekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textview_seekbar_bedroom.text = getString(R.string.category_numberstart_numberend,
                    getString(R.string.number_bedroom), minValue.toInt(),
                    maxValue.toInt())
        }

        bathroom_seekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            textview_seekbar_bathroom.text = getString(R.string.category_numberstart_numberend,
                    getString(R.string.number_bathroom), minValue.toInt(),
                    maxValue.toInt())
        }
    }

    private fun configureUI() {
        button_search_settings_property.setOnClickListener {
            val intent = Intent()
            // Surface
            intent.putExtra(MIN_SURFACE, surface_seekbar.selectedMinValue.toString().toInt())
            intent.putExtra(MAX_SURFACE, surface_seekbar.selectedMaxValue.toString().toInt())

            // Price
            intent.putExtra(MIN_PRICE, price_seekbar.selectedMinValue.toString().toInt())
            intent.putExtra(MAX_PRICE, price_seekbar.selectedMaxValue.toString().toInt())

            // Room
            intent.putExtra(MIN_ROOM, room_seekbar.selectedMinValue.toString().toInt())
            intent.putExtra(MAX_ROOM, room_seekbar.selectedMaxValue.toString().toInt())

            // Bedroom
            intent.putExtra(MIN_BEDROOM, bedroom_seekbar.selectedMinValue.toString().toInt())
            intent.putExtra(MAX_BEDROOM, bedroom_seekbar.selectedMaxValue.toString().toInt())

            // Bathroom
            intent.putExtra(MIN_BATHROOM, bathroom_seekbar.selectedMinValue.toString().toInt())
            intent.putExtra(MAX_BATHROOM, bathroom_seekbar.selectedMaxValue.toString().toInt())

            // Available
            intent.putExtra(AVAILABLE, available_spinner_search.selectedItem.toString())

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
