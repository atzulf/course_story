package com.submision.coursestory.view.upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.submision.coursestory.R
import com.submision.coursestory.data.utils.getImageUri
import com.submision.coursestory.databinding.ActivityUploadBinding
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            binding.ivPreview.setImageURI(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }
    private fun showLoading(loading: Boolean) {
        val animation = if (loading) {
            AlphaAnimation(0f, 1f).apply {
                duration = 300
            }
        } else {
            AlphaAnimation(1f, 0f).apply {
                duration = 300
            }
        }
        binding.progressBar.startAnimation(animation)
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}