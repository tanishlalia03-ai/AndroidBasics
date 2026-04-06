package com.example.androidbasics.mlkit

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.androidbasics.databinding.ActivityRecognitionBinding
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.util.concurrent.Executors
import kotlin.math.sqrt

class RecognitionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecognitionBinding
    private val gson = Gson()
    private val prefs by lazy { getSharedPreferences("FaceData", MODE_PRIVATE) }

    // Stores the relative positions of facial landmarks for the current frame
    private var currentFaceEmbedding: FloatArray? = null

    // ML Kit Face Detector Configuration
    private val faceDetector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build()
    )

    // Permission Launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize View Binding
        binding = ActivityRecognitionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 2. Check Permissions (Starts camera if successful)
        checkCameraPermission()

        // 3. Setup Button Listeners
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Save current face data to SharedPreferences
        binding.btnSave.setOnClickListener {
            currentFaceEmbedding?.let { embedding ->
                prefs.edit().putString("saved_face", gson.toJson(embedding)).apply()
                binding.tvStatus.text = "Status: Face Saved Locally!"
            } ?: run {
                binding.tvStatus.text = "Status: No Face Detected"
            }
        }

        // Match current face against saved data
        binding.btnMatch.setOnClickListener {
            val savedJson = prefs.getString("saved_face", null)
            if (savedJson != null && currentFaceEmbedding != null) {
                val savedEmbedding = gson.fromJson(savedJson, FloatArray::class.java)
                val distance = calculateDistance(currentFaceEmbedding!!, savedEmbedding)

                // Threshold logic: lower distance = better match
                if (distance < 50.0f) {
                    binding.tvStatus.text = "Status: MATCH! (Dist: ${"%.2f".format(distance)})"
                } else {
                    binding.tvStatus.text = "Status: NO MATCH (Dist: ${"%.2f".format(distance)})"
                }
            } else {
                binding.tvStatus.text = "Status: Register a face first!"
            }
        }

        // Clear saved data
        binding.btnClear.setOnClickListener {
            prefs.edit().remove("saved_face").apply()
            binding.tvStatus.text = "Status: Data cleared"
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview Use Case
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            // Analysis Use Case
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build()

            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                processImageProxy(imageProxy)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("RecognitionActivity", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.isNotEmpty()) {
                    currentFaceEmbedding = extractLandmarksAsEmbedding(faces[0])
                } else {
                    currentFaceEmbedding = null
                }
            }
            .addOnFailureListener { e ->
                Log.e("MLKit", "Face detection error: ${e.message}")
            }
            .addOnCompleteListener {
                // CRITICAL: Always close the imageProxy to allow the next frame
                imageProxy.close()
            }
    }

    private fun extractLandmarksAsEmbedding(face: Face): FloatArray {
        val landmarkList = mutableListOf<Float>()
        val types = intArrayOf(
            FaceLandmark.LEFT_EYE, FaceLandmark.RIGHT_EYE,
            FaceLandmark.NOSE_BASE, FaceLandmark.MOUTH_LEFT,
            FaceLandmark.MOUTH_RIGHT, FaceLandmark.LEFT_CHEEK,
            FaceLandmark.RIGHT_CHEEK
        )

        for (type in types) {
            val landmark = face.getLandmark(type)
            if (landmark != null) {
                // Store coordinates relative to the face's bounding box
                // This helps normalization so distance from camera matters less
                landmarkList.add(landmark.position.x - face.boundingBox.left)
                landmarkList.add(landmark.position.y - face.boundingBox.top)
            } else {
                // Consistent array size is required for distance calculation
                landmarkList.add(0f)
                landmarkList.add(0f)
            }
        }
        return landmarkList.toFloatArray()
    }

    private fun calculateDistance(emb1: FloatArray, emb2: FloatArray): Float {
        if (emb1.size != emb2.size) return Float.MAX_VALUE
        var sum = 0f
        for (i in emb1.indices) {
            val diff = emb1[i] - emb2[i]
            sum += diff * diff
        }
        return sqrt(sum.toDouble()).toFloat()
    }
}