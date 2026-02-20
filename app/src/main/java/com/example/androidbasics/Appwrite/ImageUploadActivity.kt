package com.example.androidbasics.Appwrite

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidbasics.R
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ImageUploadActivity : AppCompatActivity() {
    private val TAG = "AppwriteUpload"
    private val bucketID = "6996dc680036b04ee5f0"
    private val appwriteManager by lazy { AppwriteManager.getInstance(applicationContext) }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val file = uriToFile(uri)
            if (file != null) {
                startUpload(file)
            }
        } else {
            Log.d(TAG, "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image_upload)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startUpload(file: File) {
        lifecycleScope.launch {
            try {
                val result = appwriteManager.uploadImage(bucketID, file)

                val projectID = "6996dc3e00250d7ae563"
                val imageUrl = "https://fra.cloud.appwrite.io/v1/storage/buckets/$bucketID/files/${result.id}/view?project=$projectID"

                Log.d(TAG, "Upload Success. URL: $imageUrl")

                val imageView = findViewById<android.widget.ImageView>(R.id.uploadedImageView)

                com.bumptech.glide.Glide.with(this@ImageUploadActivity)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(imageView)

            } catch (e: Exception) {
                Log.e(TAG, "Upload Failed: ${e.message}")
                e.printStackTrace()
            }
        }
    }


    private fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            // Create a unique name to avoid overwriting cache files
            val tempFile = File(cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(tempFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            Log.e(TAG, "File conversion failed", e)
            null
        }
    }
}