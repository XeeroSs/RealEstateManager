package com.openclassrooms.realestatemanager.controller.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_property_management.*

class PropertyManagementActivity : BaseActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun getLayoutId() = R.layout.activity_property_management

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureViewModel()
        configureUI()
    }

    private fun configureUI() {
        button_save_property_management.setOnClickListener {
            checkEditTextIsEmpty()
        }
    }

    private fun checkEditTextIsEmpty() {
        if (editText_address_property_management.text.trim().isEmpty() ||
                editText_bathrooms_property_management.text.trim().isEmpty() ||
                editText_bedrooms_property_management.text.trim().isEmpty() ||
                editText_description_property_management.text.trim().isEmpty() ||
                editText_price_property_management.text.trim().isEmpty() ||
                editText_rooms_property_management.text.trim().isEmpty() ||
                editText_author_property_management.text.trim().isEmpty() ||
                editText_surface_property_management.text.trim().isEmpty() ||
                editText_type_property_management.text.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.missing_information), Toast.LENGTH_SHORT).show()
            return
        }
        createProperty()
    }

    private fun createProperty() {
        mainViewModel.createProperty(PropertyModel(surfaceProperty = editText_surface_property_management.text.toString().toInt(),
                typeProperty = editText_type_property_management.text.toString(),
                addressProperty = editText_address_property_management.text.toString(),
                priceDollarProperty = editText_price_property_management.text.toString().toInt(),
                roomsNumberProperty = editText_rooms_property_management.text.toString().toInt(),
                bedroomsNumberProperty = editText_bedrooms_property_management.text.toString().toInt(),
                bathroomsNumberProperty = editText_bathrooms_property_management.text.toString().toInt(),
                descriptionProperty = editText_description_property_management.text.toString(),
                dateProperty = Utils.getTodayDate().toString(),
                realEstateAgentProperty = editText_author_property_management.text.toString()))
        finish()
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }
}
