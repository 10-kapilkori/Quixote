package com.task.quixotetask.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.task.quixotetask.Notes
import com.task.quixotetask.UserApplication
import com.task.quixotetask.database.UserViewModel
import com.task.quixotetask.database.UserViewModelFactory
import com.task.quixotetask.databinding.ActivityAddEditBinding

private const val TAG = "AddEditActivity"

class AddEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddEditBinding
    lateinit var viewModel: UserViewModel
    var imageUri = ""
    var email = ""
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        email = intent.getStringExtra("email")!!

        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory((application as UserApplication).db.getDao())
        )[UserViewModel::class.java]

        with(binding) {
            selectPicAddEditIv.setOnClickListener {
                takeGalleryPermission()
            }

            cameraPicAddEdit.setOnClickListener {
                takeCameraPermission()
            }

            addNoteBtn.setOnClickListener {
                val title = titleEtAddEdit.text.toString()
                val description = descEtAddEdit.text.toString()

                if (title.isEmpty()) {
                    titleEtAddEdit.error = "Title required"
                    titleEtAddEdit.requestFocus()
                    return@setOnClickListener
                }

                if (title.length !in 5..100) {
                    titleEtAddEdit.error = "Title should be between 5 and 100 characters"
                    titleEtAddEdit.requestFocus()
                    return@setOnClickListener
                }

                if (description.isEmpty()) {
                    descEtAddEdit.error = "Description required"
                    descEtAddEdit.requestFocus()
                    return@setOnClickListener
                }

                if (description.length !in 100..1000) {
                    descEtAddEdit.error = "Description should be between 100 and 1000 characters"
                    descEtAddEdit.requestFocus()
                    return@setOnClickListener
                }

                if (imageUri.isEmpty()) {
                    noSelectImageAddEdit.visibility = View.VISIBLE
                }

                if (imageUri.isNotEmpty() && title.isNotEmpty() && description.isNotEmpty()) {
                    noSelectImageAddEdit.visibility = View.GONE
                    Toast.makeText(this@AddEditActivity, "Note Added", Toast.LENGTH_SHORT).show()
                    viewModel.insertNote(
                        Notes(
                            title = title,
                            description = description,
                            imagePath = imageUri,
                            email = email
                        )
                    )
                    finish()
                }
            }
        }
    }

    private fun takeCameraPermission() {
        checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        requestCameraPermission.launch(android.Manifest.permission.CAMERA)
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                requestForExternalStorage()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Allow Permission")
                    .setMessage("Please allow permission to access this feature")
                    .show()
            }
        }

    private fun takeGalleryPermission() {
        checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                callIntentForImage()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Allow Permission")
                    .setMessage("Please allow permission to access this feature")
                    .show()
            }
        }

    private fun callIntentForImageWithCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Pic")
        values.put(MediaStore.Images.Media.DESCRIPTION, "New Camera")

        uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val path = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, path, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(path[0])
        val imagePath = cursor?.getString(columnIndex!!)

        cursor?.close()
        imageUri = imagePath!!
        launchCameraActivity.launch(intent)
    }

    private fun requestForExternalStorage() {
        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        requestWriterPermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val requestWriterPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                callIntentForImageWithCamera()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Allow Permission")
                    .setMessage("Please allow permission to access this feature")
                    .show()
            }
        }

    private val launchCameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.selectPicAddEditIv.setImageURI(uri)
            }
        }

    private fun callIntentForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        launchActivity.launch(intent)
    }

    private val launchActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data

                val uri: Uri? = data?.data
                val path = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(uri!!, path, null, null, null)
                cursor?.moveToFirst()

                val columnIndex = cursor?.getColumnIndex(path[0])
                val imagePath = cursor?.getString(columnIndex!!)

                binding.selectPicAddEditIv.setImageURI(Uri.parse(imagePath))
                imageUri = imagePath ?: ""
                Log.i(TAG, "ImagePath: $imagePath")
            }
        }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}


















