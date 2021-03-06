package com.task.quixotetask.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.task.quixotetask.Notes
import com.task.quixotetask.databinding.ActivityDetailsBinding

private const val TAG = "DetailsActivity"

class DetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val note = intent.getParcelableExtra<Notes>("list")
        Log.i(TAG, "onCreate: $note")

        val title = note?.title ?: ""
        val description = note?.description ?: ""
        val imagePath = note?.imagePath ?: ""

        binding.titleDetailsTv.text = title
        binding.descriptionDetailsTv.text = description
        binding.imageDetailsIv.setImageURI(Uri.parse(imagePath))
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