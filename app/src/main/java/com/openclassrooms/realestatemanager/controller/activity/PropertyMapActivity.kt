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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controller.viewmodel.MainViewModel
import com.openclassrooms.realestatemanager.injection.Injection
import com.openclassrooms.realestatemanager.utils.PROPERTY_ID
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_property_map.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class PropertyMapActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, LocationListener, EasyPermissions.PermissionCallbacks {

    private lateinit var mapFragment: MapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var mainViewModel: MainViewModel
    private val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_map)

        setSupportActionBar(toolbar_map as Toolbar?)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        configureViewModel()
        checkPermissions()
    }

    private fun configureViewModel() {
        val viewModelProvider = Injection.provideViewModelFactory(this)
        mainViewModel = ViewModelProviders.of(this, viewModelProvider).get(MainViewModel::class.java)
    }

    private fun getProperties() {
        mainViewModel.getProperties().observe(this, Observer { properties ->
            if (properties != null) {
                for (property in properties) {
                    if (Utils.getLatLngFromAddress(property.cityProperty +
                                    property.zipCodeProperty.toString().toInt() +
                                    property.addressProperty, this) != null) {
                        googleMap.addMarker(MarkerOptions().position(Utils.getLatLngFromAddress(property.cityProperty +
                                property.zipCodeProperty.toString().toInt() +
                                property.addressProperty, this)!!)
                                .title(property.propertyId)
                                .anchor(0.5f, 0.5f))
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(123)
    private fun checkPermissions(): Boolean? {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!EasyPermissions.hasPermissions(this, *permissions)) {
            EasyPermissions.requestPermissions(this, "You need agree the permissions", 123, *permissions)
            return false
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0f, this)
        }
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0f, this)
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0f, this)
        }

        initializeMap()
        return true
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        checkPermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        checkPermissions()
    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        val intent = Intent(this, PropertyDetailsActivity::class.java)
        intent.putExtra(PROPERTY_ID, marker!!.title)
        startActivity(intent)
        return true
    }

    private fun initializeMap() {
        mapFragment = fragmentManager.findFragmentById(R.id.propert_map) as MapFragment
        mapFragment.getMapAsync {
            this.googleMap = it
            it.isMyLocationEnabled = true
            it.clear()
            if (getLocationUser() == null) {
                Toast.makeText(this, getString(R.string.not_found_localisation), Toast.LENGTH_SHORT).show()
                return@getMapAsync
            }
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocationUser(), 17f))
            getProperties()
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocationUser(): LatLng? {
        val provider = locationManager.getBestProvider(Criteria(), false) ?: return null
        val location = locationManager.getLastKnownLocation(provider) ?: return null
        return LatLng(location.latitude, location.longitude)
    }

    override fun onLocationChanged(p0: Location?) {
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
