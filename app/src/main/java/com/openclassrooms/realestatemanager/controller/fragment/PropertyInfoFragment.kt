package com.openclassrooms.realestatemanager.controller.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.property_info_fragment.*


class PropertyInfoFragment : Fragment() {

    var strtext: Int = 0

    companion object {
        fun newInstance() = PropertyInfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        strtext = arguments.getInt("PROPERTY_ID_BUNDLE")
        return inflater.inflate(R.layout.property_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textView_type_property.text = "" + strtext
    }


}
