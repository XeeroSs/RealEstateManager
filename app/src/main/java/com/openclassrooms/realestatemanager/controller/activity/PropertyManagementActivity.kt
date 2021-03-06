package com.openclassrooms.realestatemanager.controller.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapter.PropertyImageRecyclerView
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.models.ImageModel
import com.openclassrooms.realestatemanager.models.PropertyModel
import com.openclassrooms.realestatemanager.utils.*
import com.openclassrooms.realestatemanager.utils.Utils.configureViewModel
import com.openclassrooms.realestatemanager.utils.Utils.showToast
import kotlinx.android.synthetic.main.activity_property_management.*
import kotlinx.android.synthetic.main.popup_add_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import permissions.dispatcher.*
import java.io.ByteArrayOutputStream


@RuntimePermissions
class PropertyManagementActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var viewDialog: View? = null
    private var filePath: Uri? = null
    private var filePathMainPhoto: Uri? = null
    private var mainPhoto: String? = null
    private lateinit var storage: StorageReference
    private var adapter: PropertyImageRecyclerView? = null
    private val imageList = ArrayList<ImageModel>()
    private val defaultImageList = ArrayList<ImageModel>()
    private lateinit var propertyId: String
    private val imageListCompleted = ArrayList<ImageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_management)

        if (!Utils.isInternetAvailable(this)) {
            showToast(this, R.string.no_internet)
            finish()
        }

        configureViewModel(this)?.let {
            mainViewModel = it
            configureUI()
        } ?: finish()
        setSupportActionBar((management_property_toolbar as Toolbar?))
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        userInteractWithScreen()
    }

    private fun userInteractWithScreen() {
        button_add_imageMain_property_management.setOnClickListener {
            showAlertDialogGalleryOrCamera(true)
        }
        button_save_property_management.setOnClickListener { checkInformation() }
        button_add_imageList_property_management.setOnClickListener {
            configureAlertDialogForImage()
        }
    }

    // Popup with choice between camera and gallery for get image
    private fun showAlertDialogGalleryOrCamera(isMainPhoto: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_on_option))
        builder.setItems(arrayOf(getString(R.string.camera), getString(R.string.gallery))) { _, which ->
            when (which) {
                0 -> launchCameraWithPermissionCheck(if (isMainPhoto) PICK_MAIN_IMAGE_REQUEST_CAMERA
                else PICK_IMAGES_REQUEST_CAMERA)
                1 -> launchGallery(if (isMainPhoto) PICK_MAIN_IMAGE_REQUEST_GALLERY
                else PICK_IMAGES_REQUEST_GALLERY)
            }
        }
        builder.show()
    }

    private fun configureUI() {
        // RecyclerView
        recyclerView_media_property_management.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = PropertyImageRecyclerView(this, imageList, true)
        recyclerView_media_property_management.adapter = adapter

        // Get the property id to get its data which are displayed to the user.
        // If the id is null, the user wants to create a new property,
        // nothing is happening.
        intent.getStringExtra(PROPERTY_UPDATE)?.let { propertyId ->
            mainViewModel.getProperty(propertyId)?.observe(this, Observer {
                editText_surface_property_management.text = it.surfaceProperty.toString().toEditable()
                editText_address_property_management.text = it.addressProperty.toEditable()
                editText_type_property_management.text = it.typeProperty.toEditable()
                editText_addaddress_property_management.text = it.addAddressProperty.toEditable()
                editText_zipcode_property_management.text = it.zipCodeProperty.toString().toEditable()
                editText_city_property_management.text = it.cityProperty.toEditable()
                editText_price_property_management.text = it.priceDollarProperty.toString().toEditable()
                editText_rooms_property_management.text = it.roomsNumberProperty.toString().toEditable()
                editText_bedrooms_property_management.text = it.bedroomsNumberProperty.toString().toEditable()
                editText_bathrooms_property_management.text = it.bathroomsNumberProperty.toString().toEditable()
                editText_description_property_management.text = it.descriptionProperty.toEditable()
                editText_author_property_management.text = it.realEstateAgentProperty.toEditable()
                if (it.statusProperty) radioGroup_available.isChecked = true else
                    radioGroup_sold.isChecked = true
                Glide.with(this).load(it.photosProperty).into(imageMain_property_management)
                mainPhoto = it.photosProperty
                this.propertyId = it.propertyId
                storage = FirebaseStorage.getInstance().getReference(it.propertyId)

                mainViewModel.getImages(it.propertyId)?.observe(this, Observer { images ->
                    imageList.addAll(images)
                    defaultImageList.addAll(images)
                    adapter?.notifyDataSetChanged()
                })
            })
        } ?: mainViewModel.getPropertyId()?.let {
            // Initialize property Id
            propertyId = it
            // Get the image reference in Firebase
            storage = FirebaseStorage.getInstance().getReference(it)
        } ?: finish()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun isAvailable() = radioGroup_available.isChecked


    // Check before create/modify a property that everything is correct
    private fun checkInformation() {
        if (editText_address_property_management.text?.trim()?.isEmpty()!! ||
                editText_zipcode_property_management.text?.trim()?.isEmpty()!! ||
                editText_city_property_management.text?.trim()?.isEmpty()!! ||
                editText_bathrooms_property_management.text?.trim()?.isEmpty()!! ||
                editText_bedrooms_property_management.text?.trim()?.isEmpty()!! ||
                editText_description_property_management.text?.trim()?.isEmpty()!! ||
                editText_price_property_management.text?.trim()?.isEmpty()!! ||
                editText_rooms_property_management.text?.trim()?.isEmpty()!! ||
                editText_author_property_management.text?.trim()?.isEmpty()!! ||
                editText_surface_property_management.text?.trim()?.isEmpty()!! ||
                editText_type_property_management.text?.trim()?.isEmpty()!!) {
            Toast.makeText(this, getString(R.string.missing_information), Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the address entered is correct
        if (Utils.getLatLngFromAddress("${editText_address_property_management.text}, " +
                        "${editText_addaddress_property_management.text} " +
                        "${editText_zipcode_property_management.text.toString().toInt()} " +
                        "${editText_city_property_management.text}"
                        , this) == null) {
            Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show()
            return
        }
        button_save_property_management.isEnabled = false
        uploadImages()
    }

    private fun createNewProperty(mainPhoto: String, IMAGES: ArrayList<ImageModel>) {
        val property = PropertyModel(surfaceProperty = editText_surface_property_management.text.toString().toInt(),
                typeProperty = editText_type_property_management.text.toString(),
                addressProperty = editText_address_property_management.text.toString(),
                addAddressProperty = editText_addaddress_property_management.text.toString(),
                zipCodeProperty = editText_zipcode_property_management.text.toString().toInt(),
                cityProperty = editText_city_property_management.text.toString(),
                priceDollarProperty = editText_price_property_management.text.toString().toInt(),
                roomsNumberProperty = editText_rooms_property_management.text.toString().toInt(),
                bedroomsNumberProperty = editText_bedrooms_property_management.text.toString().toInt(),
                bathroomsNumberProperty = editText_bathrooms_property_management.text.toString().toInt(),
                descriptionProperty = editText_description_property_management.text.toString(),
                dateProperty = Utils.todayDate,
                propertyContentProviderId = System.currentTimeMillis() / 1000,
                realEstateAgentProperty = editText_author_property_management.text.toString(),
                statusProperty = isAvailable(),
                saleDateProperty = if (isAvailable()) "Not sold" else Utils.todayDate,
                photosProperty = mainPhoto)

        intent.getStringExtra(PROPERTY_UPDATE)?.let { _ ->
            // Update property
            mainViewModel.updateProperty(propertyId, property, this, IMAGES)
        } ?: /*Create property*/mainViewModel.createProperty(property, this, propertyId, IMAGES)

        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        data?.let {
            when (requestCode) {
                // Photos list from gallery
                PICK_IMAGES_REQUEST_GALLERY -> {
                    viewDialog?.let { view ->
                        filePath = it.data
                        Glide.with(this).load(filePath).into(view.popupAddItem_ButtonImage)
                    }
                }
                // Main photo from gallery
                PICK_MAIN_IMAGE_REQUEST_GALLERY -> {
                    filePathMainPhoto = it.data
                    Glide.with(this).load(filePathMainPhoto).into(imageMain_property_management)
                }
                // Main photo from camera
                PICK_MAIN_IMAGE_REQUEST_CAMERA -> {
                    it.extras?.let { extras ->
                        val bitMap = extras.get("data") as Bitmap
                        filePathMainPhoto = getImageUri(bitMap)
                        imageMain_property_management.setImageBitmap(bitMap)
                    }
                }
                // Photos list from camera
                PICK_IMAGES_REQUEST_CAMERA -> {
                    viewDialog?.let { view ->
                        it.extras?.let { extras ->
                            val imageBitmap = extras.get("data") as Bitmap
                            view.popupAddItem_ButtonImage.setImageBitmap(imageBitmap)
                            filePath = getImageUri(imageBitmap)
                        }
                    }
                }
                else -> return
            }
        }
    }

    private fun getImageUri(photo: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, photo, "", null)
        return Uri.parse(path)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    // Adding image for photos list
    private fun configureAlertDialogForImage() {
        viewDialog = LayoutInflater.from(this).inflate(R.layout.popup_add_item, findViewById(android.R.id.content), false)
        viewDialog?.let {
            val alertDialog = AlertDialog.Builder(this).setView(viewDialog).show()
            it.popupAddItem_ButtonImage.setOnClickListener {
                showAlertDialogGalleryOrCamera(false)
            }
            it.popupAddItem_Cancel.setOnClickListener { alertDialog.dismiss() }
            it.popupAddItem_Add.setOnClickListener { _ ->
                if (it.popupAddItem_Name.text.toString().trim().isEmpty()) {
                    showToast(this, R.string.missing_information)
                    return@setOnClickListener
                }
                filePath?.let { _ ->
                    // Add image URI for recyclerView
                    imageList.add(ImageModel(imageURL = filePath.toString(),
                            imageLabel = it.popupAddItem_Name.text.toString(),
                            imageId = propertyId))
                    filePath = null
                    alertDialog.dismiss()
                    adapter?.notifyDataSetChanged()
                } ?: showToast(this, R.string.missing_information)
            }
        } ?: finish()
    }

    // Launch gallery
    private fun launchGallery(pickImageRequest: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest)
    }

    // Launch Camera
    @NeedsPermission(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun launchCamera(pickImageRequest: Int) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, pickImageRequest)
            }
        }
    }

    @OnShowRationale(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForPermission(request: PermissionRequest) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.need_permission))
                .setMessage(getString(R.string.need_permission_text))
                .setPositiveButton(android.R.string.yes) { _, _ -> request.proceed() }
                .setNegativeButton(android.R.string.no) { _, _ -> request.cancel() }
                .show()
    }

    @OnPermissionDenied(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionDenied() {
        Toast.makeText(this, getString(R.string.missing_permission), Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onPermissionNeverAskAgain() {
        Toast.makeText(this, getString(R.string.missing_permission), Toast.LENGTH_SHORT).show()
    }

    // photos upload in firebase database
    private fun uploadImages() {
        GlobalScope.launch(Dispatchers.IO) {
            var mainImage = ""
            filePathMainPhoto?.let {
                val imageMainUrl = putAndGetImage("main", filePathMainPhoto.toString())
                mainImage = imageMainUrl
            } ?: run {
                mainImage = mainPhoto.toString()
            }

            for (image in imageList) {
                if (!image.imageURL.startsWith("https://")) {
                    val imageURL = putAndGetImage(imageListCompleted.size.toString(), image.imageURL)
                    imageListCompleted.add(ImageModel(imageURL = imageURL,
                            imageLabel = image.imageLabel,
                            imageId = propertyId))
                } else imageListCompleted.add(ImageModel(imageURL = image.imageURL,
                        imageLabel = image.imageLabel,
                        imageId = propertyId))
            }

            val urlList = ArrayList<String>()
            imageListCompleted.forEach { image -> urlList.add(image.imageURL) }
            deleteImagesFromFirebase(urlList)

            /*    defaultImageList.forEach { image ->
                    if (!imageListCompleted.contains(image)) mainViewModel.deleteImage(image.id)
                }*/
            createNewProperty(mainImage, imageListCompleted)
        }
    }

    private suspend fun deleteImagesFromFirebase(IMAGES: ArrayList<String>) {
        var namePhotoIndex = 0
        var emptyImage = false
        var image: Uri?
        do {
            image = try {
                storage.child("$namePhotoIndex.jpg").downloadUrl.await()
            } catch (e: StorageException) {
                null
            }
            if (image == null) emptyImage = true else {
                if (!IMAGES.contains(image.toString())) storage.child("$namePhotoIndex.jpg").delete().await()
                namePhotoIndex++
            }
        } while (!emptyImage)
    }

    private suspend fun putAndGetImage(namePhoto: String, key: String): String {
        return storage.child("$namePhoto.jpg").putFile(Uri.parse(key))
                .await().storage.downloadUrl.await().toString()
    }
}
