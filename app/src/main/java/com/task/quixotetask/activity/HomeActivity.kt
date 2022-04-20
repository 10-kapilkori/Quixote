package com.task.quixotetask.activity

import android.content.Intent
import android.content.SharedPreferences
import android.icu.lang.UCharacter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.task.quixotetask.Notes
import com.task.quixotetask.NotesAdapter
import com.task.quixotetask.UserApplication
import com.task.quixotetask.database.UserViewModel
import com.task.quixotetask.database.UserViewModelFactory
import com.task.quixotetask.databinding.ActivityHomeBinding

private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity(), NotesAdapter.OnClickEvents {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: UserViewModel
    lateinit var prefs: SharedPreferences
    lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")
        prefs = getSharedPreferences("email", MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString("email", email)
        editor.apply()

        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory((application as UserApplication).db.getDao())
        )[UserViewModel::class.java]

        var list: List<Notes> = ArrayList()
        adapter = NotesAdapter(list, this)

        viewModel.fetchUserNote(email!!).observe(this) {
            if (it != null) {
                Log.i(TAG, "onCreate: $it")
                list = it
                adapter.updatedList(list)
            }
        }

        binding.homeRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.homeRv.adapter = adapter

        with(binding) {
            addNoteFab.setOnClickListener {
                startActivity(
                    Intent(
                        this@HomeActivity,
                        AddEditActivity::class.java
                    ).putExtra("email", prefs.getString("email", email))
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.edit().clear().apply()
    }

    override fun onCardClick(list: List<Notes>, position: Int) {
        startActivity(
            Intent(
                this, DetailsActivity::class.java
            ).putExtra("list", list[position])
        )
    }
}














