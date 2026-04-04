package com.example.androidbasics.mlkit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidbasics.R
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class BarCodeScanningActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {

    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var btnSwitchFlashLight: Button
    private var isFlashON = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bar_code_scanning)

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)
        btnSwitchFlashLight = findViewById(R.id.btn_switch_flashlight)

        // Set up Torch (Flashlight) listener
        barcodeScannerView.setTorchListener(this)

        // Initialize the captureManager to handle barcode logic
        capture = CaptureManager(this, barcodeScannerView)
        capture.initializeFromIntent(intent, savedInstanceState)

        // Start the scanning loop
        startScanning()

        // Flashlight toggle logic
        btnSwitchFlashLight.setOnClickListener {
            if (isFlashON) {
                barcodeScannerView.setTorchOff()
            } else {
                barcodeScannerView.setTorchOn()
            }
        }
    }

    private fun startScanning() {
        barcodeScannerView.decodeSingle { result ->
            val scannedText = result.text

            if (!scannedText.isNullOrEmpty()) {
                // Check if the scanned text is a web link
                if (scannedText.startsWith("http://") || scannedText.startsWith("https://") || scannedText.startsWith("www.")) {
                    openBrowser(scannedText)
                } else {
                    // It's just plain text, show it in a Toast
                    Toast.makeText(this, "Scanned: $scannedText", Toast.LENGTH_LONG).show()
                }
            }

            // Call again to allow continuous scanning without restarting activity
            // Using a small delay or post-delayed is sometimes better to prevent "double-scans"
            barcodeScannerView.postDelayed({
                startScanning()
            }, 2000) // 2-second gap before it allows another scan
        }
    }

    private fun openBrowser(url: String) {
        try {
            // Fix URL if it starts with www. but lacks http protocol
            val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(formattedUrl)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Lifecycle Methods ---
    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    // --- Torch Listener Methods ---
    override fun onTorchOn() {
        isFlashON = true
        btnSwitchFlashLight.text = "Turn off Flash"
    }

    override fun onTorchOff() {
        isFlashON = false
        btnSwitchFlashLight.text = "Turn on Flash"
    }
}