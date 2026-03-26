package com.example.androidbasics.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidbasics.databinding.ActivityLocationBinding
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // Stop updates immediately after getting a result to save battery
                fusedLocationClient.removeLocationUpdates(this)

                locationResult.lastLocation?.let { location ->
                    updateUI(location)
                }
            }
        }

        binding.btnGetLocation.setOnClickListener {
            checkLocationPermissions()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        binding.tvAddress.text = "Fetching coordinates..."

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMaxUpdates(1) // We only need the address once
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun updateUI(location: Location) {
        val lat = location.latitude
        val lng = location.longitude

        binding.tvLatitude.text = "Latitude: $lat"
        binding.tvLongitude.text = "Longitude: $lng"
        binding.tvAddress.text = "Fetching address..."

        lifecycleScope.launch {
            val address = getAddressFromLocation(lat, lng)
            binding.tvAddress.text = "Address: $address"
        }
    }

    private suspend fun getAddressFromLocation(lat: Double, lng: Double): String {
        return withContext(Dispatchers.IO) {
            val geocoder = Geocoder(this@LocationActivity, Locale.getDefault())

            try {
                // Modern API (Android 13+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine { continuation ->
                        geocoder.getFromLocation(lat, lng, 1, object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                if (addresses.isNotEmpty()) {
                                    continuation.resume(addresses[0].getAddressLine(0))
                                } else {
                                    continuation.resume("Address not found")
                                }
                            }
                            override fun onError(errorMessage: String?) {
                                continuation.resume("Error: $errorMessage")
                            }
                        })
                    }
                } else {
                    // Legacy API (Below Android 13)
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lng, 1)
                    if (!addresses.isNullOrEmpty()) {
                        addresses[0].getAddressLine(0)
                    } else {
                        "Address not found"
                    }
                }
            } catch (e: Exception) {
                "Service Unavailable: ${e.localizedMessage}"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}