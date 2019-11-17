package com.openclassrooms.realestatemanager.controller.activity

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_property_details.*

class PropertyDetailsActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_property_details
    private lateinit var propertyId: String
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureViewModel()
        getPropertyId()
    }

    private fun getPropertyId() {
        propertyId = intent.getStringExtra(PROPERTY_ID)
        getProperty()
    }

    private fun getProperty() {
        mainViewModel.getProperty(propertyId).observe(this, Observer { property ->
            if (property == null) finish()
            configureUI(property!!)
        })
    }

    private fun configureUI(property: PropertyModel) {
        fun getTextView(textView: TextView, text: String) {
            textView.text = "${textView.text} $text"
        }
        getTextView(textView_author_property, property.realEstateAgentProperty)
        getTextView(textView_bathrooms_property, property.bathroomsNumberProperty.toString())
        getTextView(textView_bedrooms_property, property.bedroomsNumberProperty.toString())
        getTextView(textView_description_property_fragment, property.descriptionProperty)
        getTextView(textView_entry_sale_property, Utils.todayDate)
        getTextView(textView_price_property, property.priceDollarProperty.toString() + "$")
        getTextView(textView_rooms_property, property.roomsNumberProperty.toString())
        getTextView(textView_sale_date_property, property.saleDateProperty)
        getTextView(textView_status_property, if (property.statusProperty) getString(R.string.available) else getString(R.string.not_available))
        getTextView(textView_type_property, property.typeProperty)
        getTextView(textView_surface_property, property.surfaceProperty.toString() + "m")
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }
}