package com.openclassrooms.realestatemanager.controller.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.property_info_fragment.*


class PropertyInfoFragment : Fragment() {

    private var propertyId: Int = 0
    private lateinit var mainViewModel: MainViewModel

    companion object {
        fun newInstance() = PropertyInfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        propertyId = arguments.getInt("PROPERTY_ID_BUNDLE")
        return inflater.inflate(R.layout.property_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()
        getProperty()
    }

    private fun getProperty() {
        mainViewModel.getProperty(propertyId).observe(this, Observer { property ->
            if (property == null) activity.onBackPressed()
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
        getTextView(textView_entry_sale_property, Utils.getTodayDate())
        getTextView(textView_price_property, property.priceDollarProperty.toString() + "$")
        getTextView(textView_rooms_property, property.roomsNumberProperty.toString())
        getTextView(textView_sale_date_property, property.saleDateProperty)
        getTextView(textView_status_property, if (property.statusProperty) getString(R.string.available) else getString(R.string.not_available))
        getTextView(textView_type_property, property.typeProperty)
        getTextView(textView_surface_property, property.surfaceProperty.toString() + "m")
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(activity)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }
}
