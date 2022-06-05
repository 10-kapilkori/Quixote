package com.task.quixotetask.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.task.quixotetask.UserApplication
import com.task.quixotetask.Users
import com.task.quixotetask.database.UserViewModel
import com.task.quixotetask.database.UserViewModelFactory
import com.task.quixotetask.databinding.ActivitySignUpBinding

private const val TAG = "SignUpActivity"

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var viewModel: UserViewModel
    var charCount = 0
    var numberCount = 0
    var symbolCount = 0

    companion object {
        var userList: List<Users> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory((application as UserApplication).db.getDao())
        )[UserViewModel::class.java]

        viewModel.getAllUsers().observe(this) {
            userList = it
        }

        with(binding) {
            createAccountBtn.setOnClickListener {
                val name = usernameEtSignUp.text.toString()
                val phone = phoneEtSignUp.text.toString()
                val email = emailEtSignUp.text.toString().trim()
                val confirmPass = confirmPasswordEtSignUp.text.toString()
                val password = passwordEtSignUp.text.toString()

                val result = validateDetails(name, phone, email, confirmPass, password)

                if (result) {
                    viewModel.insertUser(Users(name, email, phone, password))

                    Toast.makeText(this@SignUpActivity, "User Created", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    return@setOnClickListener
                }
            }

            alreadyUserSignUpTv.setOnClickListener {
                startActivity(
                    Intent(
                        this@SignUpActivity,
                        MainActivity::class.java
                    )
                )
                finish()
            }
        }
    }

    fun validateDetails(
        name: String,
        phone: String,
        email: String,
        confirmPass: String,
        password: String
    ): Boolean {
        with(binding) {
            if (email.isEmpty()) {
                emailEtSignUp.error = "Email required"
                emailEtSignUp.requestFocus()
                return false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEtSignUp.error = "Invalid Email"
                emailEtSignUp.requestFocus()
                return false
            }

            if (name.isEmpty()) {
                usernameEtSignUp.error = "Username required"
                usernameEtSignUp.requestFocus()
                return false
            }

            if (name.length !in 4..25) {
                usernameEtSignUp.error = "Username should be between 4 and 25 characters"
                usernameEtSignUp.requestFocus()
                return false
            }

            if (phone.isEmpty()) {
                phoneEtSignUp.error = "Phone Number required"
                phoneEtSignUp.requestFocus()
                return false
            }

            if (phone.length != 10 || phone[0] !in '6'..'9') {
                phoneEtSignUp.error = "Invalid Phone Number"
                phoneEtSignUp.requestFocus()
                return false
            }

            if (password.isEmpty()) {
                passwordEtSignUp.error = "Password required"
                passwordEtSignUp.requestFocus()
                return false
            }

            for (i in password.indices) {
                if (password[i] in 'A'..'Z') {
                    charCount++
                }
                if (password[i] in '0'..'9') {
                    numberCount++
                }
                if (password[i] !in 'a'..'z' && password[i] !in 'A'..'Z' && password[i] !in '0'..'9') {
                    symbolCount++
                }
            }

            if (password[0] !in 'a'..'z') {
                passwordEtSignUp.error = "Password should start with lowercase characters"
                passwordEtSignUp.requestFocus()
                return false
            }
            if (password.length !in 8..15) {
                passwordEtSignUp.error = "Password length should be between 8 and 15 characters"
                passwordEtSignUp.requestFocus()
                return false
            }
            if (password.lowercase().contains(name.lowercase())) {
                passwordEtSignUp.error = "Password cannot contain your name"
                passwordEtSignUp.requestFocus()
                return false
            }
            if (charCount < 2) {
                passwordEtSignUp.error = "Password should contain >=2 Uppercase characters"
                passwordEtSignUp.requestFocus()
                return false
            }
            if (numberCount < 2) {
                passwordEtSignUp.error = "Password should contain >=2 Digits"
                passwordEtSignUp.requestFocus()
                return false
            }
            if (symbolCount < 1) {
                passwordEtSignUp.error = "Password should contain >=1 Special Character"
                passwordEtSignUp.requestFocus()
                return false
            }

            if (confirmPass.isEmpty()) {
                confirmPasswordEtSignUp.error = "Confirm Password required"
                confirmPasswordEtSignUp.requestFocus()
                return false
            }

            if (confirmPass != password) {
                confirmPasswordEtSignUp.error = "Password Does not matches"
                confirmPasswordEtSignUp.requestFocus()
                return false
            }
        }

        return true
    }

}















