package com.openclassrooms.realestatemanager.controller.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.controller.fragment.PropertyInfoFragment
import com.openclassrooms.realestatemanager.controller.fragment.PropertyLocationFragment
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import kotlinx.android.synthetic.main.property_activity.*


class PropertyActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.property_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val propertyId = intent.getIntExtra(PROPERTY_ID, -1)

        if (propertyId == -1) finish()

        val bundle = Bundle()
        bundle.putInt("PROPERTY_ID_BUNDLE", propertyId)

        if (savedInstanceState == null) {
            transactionFragment(PropertyInfoFragment.newInstance(), bundle)
        }

        activity_main_bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_bottom_navigation_menu_information -> {
                    transactionFragment(PropertyInfoFragment.newInstance(), bundle)
                }
                R.id.action_bottom_navigation_menu_location -> {
                    transactionFragment(PropertyLocationFragment.newInstance(), bundle)
                }
            }
            false
        }
    }

    private fun transactionFragment(propertyFragment: Fragment, bundle: Bundle) {
        propertyFragment.setArguments(bundle)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, propertyFragment)
                .commitNow()
    }

}
