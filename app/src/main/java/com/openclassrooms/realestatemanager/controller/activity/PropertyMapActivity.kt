package com.openclassrooms.realestatemanager.controller.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.showToast
import kotlinx.android.synthetic.main.activity_property_map.*
import permissions.dispatcher.*


@RuntimePermissions
class PropertyMapActivity : AppCompatActivity(), LocationListener {

    private var googleMap: GoogleMap? = null
    private var locationManager: LocationManager? = null
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_map)

        // Get the fragment which displayed the map
        fragmentManager.findFragmentById(R.id.propert_map)?.let {
            mapFragment = it as MapFragment
        } ?: finish()

        setSupportActionBar(toolbar_map as Toolbar?)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        // Calls the permissions check to locate the user
        initializeLocationWithPermissionCheck()
    }

    // Gets properties
    private fun getProperties() {
        Utils.configureViewModel(this)?.let { viewModel ->
            viewModel.getProperties().observe(this, Observer { properties ->
                properties?.let {
                    // Properties is locate with at address and place on the map
                    properties.forEach {
                        Utils.getLatLngFromAddress(it.cityProperty +
                                it.zipCodeProperty.toString().toInt() +
                                it.addressProperty, this)?.let { latLng ->
                            googleMap?.addMarker(MarkerOptions().position(latLng)
                                    .title(it.propertyId)
                                    .anchor(0.5f, 0.5f))
                        }
                    }
                } ?: finish()
            })
        } ?: finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun initializeLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Get user location
        locationManager?.let {
            if (it.isProviderEnabled(LocationManager.GPS_PROVIDER)) it.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0f, this)
            if (it.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) it.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0f, this)
            if (it.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) it.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0f, this)
            initializeMap()
        }
    }

    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForPermission(request: PermissionRequest) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.need_permission))
                .setMessage(getString(R.string.need_permission_text))
                .setPositiveButton(android.R.string.yes) { _, _ -> request.proceed() }
                .setNegativeButton(android.R.string.no) { _, _ -> request.cancel() }
                .show()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun onPermissionDenied() {
        Toast.makeText(this, getString(R.string.missing_permission), Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun onPermissionNeverAskAgain() {
        Toast.makeText(this, getString(R.string.missing_permission), Toast.LENGTH_SHORT).show()
    }

    // Initialize map
    private fun initializeMap() {
        mapFragment.getMapAsync {
            this.googleMap = it
            // Enable user location
            it.isMyLocationEnabled = true
            // Clear markers
            it.clear()
            // Place properties
            getProperties()
            // Move camera on the user
            getLocationUser()?.let { latLng ->
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
            } ?: showToast(this, R.string.not_found_localisation)
            // Interact with marker, launch property activity details
            it.setOnMarkerClickListener { marker ->
                val intent = Intent(this, PropertyDetailsActivity::class.java)
                intent.putExtra(PROPERTY_ID, marker.title)
                startActivity(intent)
                return@setOnMarkerClickListener true
            }
        }
    }


    // Get user location
    @SuppressLint("MissingPermission")
    private fun getLocationUser(): LatLng? {
        locationManager?.let {
            val provider = it.getBestProvider(Criteria(), false) ?: return null
            val location = it.getLastKnownLocation(provider) ?: return null
            return LatLng(location.latitude, location.longitude)
        } ?: return null
    }

    override fun onLocationChanged(p0: Location?) {}

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
