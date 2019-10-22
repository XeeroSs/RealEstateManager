package com.openclassrooms.realestatemanager.controller.activity

import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.models.PropertyModel
import kotlinx.android.synthetic.main.activity_property_management.*

class PropertyManagementActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_property_management

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*button_save_property_management.setOnClickListener {
            val database = RealEstateManagerDatabase.getInstance(this)
            val property = PropertyModel(
                    typeProperty = editText_type_property_management.text.toString(),
                    addressProperty = editText_address_property_management.text.toString(),
                    priceDollarProperty = editText_price_property_management.text.toString())
            database!!.propertyListDao().insertListProperty(property)
        }*/
    }
}
