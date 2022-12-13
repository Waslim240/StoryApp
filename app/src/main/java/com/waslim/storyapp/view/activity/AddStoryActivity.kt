package com.waslim.storyapp.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivityAddStoryBinding
import com.waslim.storyapp.model.*
import com.waslim.storyapp.viewmodel.AddStoryViewModel
import com.waslim.storyapp.viewmodel.UserTokenViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel by viewModels<AddStoryViewModel>()
    private val userTokenViewModel by viewModels<UserTokenViewModel>()
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = getString(R.string.tambah_story_baru)

        binding.btnUpload.setOnClickListener {
            uploadStory()
            closedKeyboard()
        }
        dialogCustom()
    }

    private fun dialogCustom() = binding.takeAPicture.setOnClickListener {
        when {
            !allPermissionsGranted() -> ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.custom_alert_dialog)

        dialog.findViewById<TextView>(R.id.tv_gallery)?.setOnClickListener {
            startGallery()
            dialog.cancel()
        }

        dialog.findViewById<TextView>(R.id.tv_cameraX)?.setOnClickListener {
            startCameraX()
            dialog.cancel()
        }

        dialog.findViewById<TextView>(R.id.tv_camera)?.setOnClickListener {
            startTakePhoto()
            dialog.cancel()
        }

        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            requestCode == REQUEST_CODE_PERMISSIONS && !allPermissionsGranted() -> {
                showToast(getString(R.string.tidak_mendapatkan_permission))
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.pilih_gambar))
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                applicationContext,
                AUTHORITY,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            CAMERA_X_RESULT -> {
                val myFile = it.data?.getSerializableExtra(PICTURE) as File
                val isBackCamera = it.data?.getBooleanExtra(IS_BACK_CAMERA, true) as Boolean

                getFile = myFile
                val result = rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path),
                    isBackCamera
                )

                binding.previewImage.setImageBitmap(result)
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            RESULT_OK -> {
                val myFile = File(currentPhotoPath)
                getFile = myFile

                val result = BitmapFactory.decodeFile(getFile?.path)
                binding.previewImage.setImageBitmap(result)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val selectedImg: Uri = result.data?.data as Uri

                val myFile = uriToFile(selectedImg, applicationContext)

                getFile = myFile

                binding.previewImage.setImageURI(selectedImg)
            }
        }
    }

    private fun uploadStory() = when {
        getFile != null && binding.tvDescriptionAdd.text.toString().isNotEmpty()-> {
            val file = reduceFileImage(getFile as File)

            val description = binding.tvDescriptionAdd.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            userTokenViewModel.getToken().observe(this@AddStoryActivity) {
                when {
                    it != "" -> addStory(it, imageMultipart, description)
                }
            }
        }
        binding.tvDescriptionAdd.text.toString().isEmpty() -> showToast(getString(R.string.deskripsi_harus_isi))
        else -> showToast(getString(R.string.gambar_harus_ada))
    }

    private fun addStory(token: String, imageFile: MultipartBody.Part, description: RequestBody) {
        showLoading(true)
        if (token != "") {
            addStoryViewModel.addNewStory(token, imageFile, description)
            Handler(Looper.getMainLooper()).postDelayed({
                showLoading(false)
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
                showToast( getString(R.string.berhasil_menambah_story))
            }, DELAY)
        }
    }

    private fun showLoading(isLoading: Boolean) = addStoryViewModel.isLoading.observe(this) {
        binding.progressBarAddStory.visibility = when {
            isLoading -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun closedKeyboard() {
        val view: View? = currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        const val PICTURE = "picture"
        const val IS_BACK_CAMERA = "isBackCamera"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val AUTHORITY = "com.waslim.storyapp"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val DELAY = 3000L
    }

}