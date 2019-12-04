package com.openclassrooms.realestatemanager.controller.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.adapter.PropertyImageRecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.base.BaseActivity
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.PICK_IMAGE_REQUEST
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_property_management.*
import kotlinx.android.synthetic.main.popup_add_item.view.*

class PropertyManagementActivity : BaseActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewDialog: View
    private var filePath: Uri? = null
    lateinit var adapter: PropertyImageRecyclerView
    var propertiesImagesList = ArrayList<LinkedHashMap<String, String>>()

    override fun getLayoutId() = R.layout.activity_property_management

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureViewModel()
        configureUI()
    }

    private fun configureUI() {
        recyclerView_media_property_management.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = PropertyImageRecyclerView(this, propertiesImagesList)
        recyclerView_media_property_management.adapter = adapter
        button_save_property_management.setOnClickListener { checkInformation() }
        imageView_button_add_property_management.setOnClickListener { configureAlertDialogForImage() }
    }

    private fun checkInformation() {
        if (editText_address_property_management.text.trim().isEmpty() ||
                editText_zipcode_property_management.text.trim().isEmpty() ||
                editText_city_property_management.text.trim().isEmpty() ||
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
        if (Utils.getLatLngFromAddress("${editText_address_property_management.text}, " +
                        "${editText_addaddress_property_management.text} " +
                        "${editText_zipcode_property_management.text.toString().toInt()} " +
                        "${editText_city_property_management.text}"
                        , this) == null) {
            Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, getString(R.string.save_property), Toast.LENGTH_SHORT).show()
        createNewProperty()
    }

    private fun createNewProperty() {
        mainViewModel.createProperty(PropertyModel(surfaceProperty = editText_surface_property_management.text.toString().toInt(),
                typeProperty = editText_type_property_management.text.toString(),
                addressProperty = "${editText_address_property_management.text.toString()}, " +
                        "${editText_addaddress_property_management.text.toString()}, " +
                        "${editText_zipcode_property_management.text.toString().toInt()} " +
                        "${editText_city_property_management.text.toString()}",
                priceDollarProperty = editText_price_property_management.text.toString().toInt(),
                roomsNumberProperty = editText_rooms_property_management.text.toString().toInt(),
                bedroomsNumberProperty = editText_bedrooms_property_management.text.toString().toInt(),
                bathroomsNumberProperty = editText_bathrooms_property_management.text.toString().toInt(),
                descriptionProperty = editText_description_property_management.text.toString(),
                dateProperty = Utils.todayDate,
                photosPropertyJSON = Utils.serializeArrayList(propertiesImagesList),
                realEstateAgentProperty = editText_author_property_management.text.toString()), this)
        finish()
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null || data!!.data != null) {
                filePath = data.data
                viewDialog.popupAddItem_ButtonImage.setImageBitmap(
                        MediaStore.Images.Media.getBitmap(contentResolver, filePath))
            }
        }
    }

    private fun configureAlertDialogForImage() {
        viewDialog = LayoutInflater.from(this).inflate(R.layout.popup_add_item, null)
        val alertDialog = AlertDialog.Builder(this).setView(viewDialog).show()
        viewDialog.popupAddItem_ButtonImage.setOnClickListener {
            launchGallery()
        }
        viewDialog.popupAddItem_Cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        viewDialog.popupAddItem_Add.setOnClickListener {
            if (filePath != null && viewDialog.popupAddItem_Name.text.toString().trim().isNotEmpty()) {
                val imagePropertyModel = LinkedHashMap<String, String>()
                imagePropertyModel[filePath.toString()] = viewDialog.popupAddItem_Name.text.toString()
                propertiesImagesList.add(imagePropertyModel)
                filePath = null
                alertDialog.dismiss()
                adapter.notifyDataSetChanged()
            } else Toast.makeText(this, getString(R.string.missing_information), Toast.LENGTH_SHORT).show()

        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
}
