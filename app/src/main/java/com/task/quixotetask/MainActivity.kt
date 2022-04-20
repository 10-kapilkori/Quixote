package com.task.quixotetask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.task.quixotetask.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory((application as UserApplication).db.getDao())
        )[UserViewModel::class.java]

        with(binding) {
            loginBtn.setOnClickListener {
                val email = emailEtSignIn.text.toString().trim()
                val password = passwordEtSignIn.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty())
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        viewModel.fetchUser(email, password).observe(this@MainActivity) {
                            validateUser(it)
                        }
                    } else {
                        viewModel.fetchUserPhone(email, password).observe(this@MainActivity) {
                            validateUser(it)
                        }
                    }

                viewModel.getAllUsers().observe(this@MainActivity) {
                    Log.i(TAG, "onCreate: $it")
                }
            }

            newUserSignInTv.setOnClickListener {
                startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
            }
        }
    }

    private fun validateUser(it: Users?) {
        if (it != null) {
            Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_SHORT)
                .show()
            startActivity(
                Intent(
                    this@MainActivity,
                    HomeActivity::class.java
                ).putExtra("email", it.email)
            )
            finish()
        } else {
            Toast.makeText(
                this@MainActivity,
                "Invalid User",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}