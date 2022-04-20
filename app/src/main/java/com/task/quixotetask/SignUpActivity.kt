package com.task.quixotetask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.task.quixotetask.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

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

                if (email.isEmpty()) {
                    emailEtSignUp.error = "Email required"
                    emailEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEtSignUp.error = "Invalid Email"
                    emailEtSignUp.requestFocus()
                }

                if (name.isEmpty()) {
                    usernameEtSignUp.error = "Username required"
                    usernameEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (name.length !in 4..25) {
                    usernameEtSignUp.error = "Username should be between 4 and 25 characters"
                    usernameEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (phone.isEmpty()) {
                    phoneEtSignUp.error = "Phone Number required"
                    phoneEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (phone.length != 10 || phone[0] !in '7'..'9') {
                    phoneEtSignUp.error = "Invalid Phone Number"
                    phoneEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    passwordEtSignUp.error = "Password required"
                    passwordEtSignUp.requestFocus()
                    return@setOnClickListener
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
                    return@setOnClickListener
                }
                if (password.length !in 8..15) {
                    passwordEtSignUp.error = "Password length should be between 8 and 15 characters"
                    passwordEtSignUp.requestFocus()
                    return@setOnClickListener
                }
                if (name.lowercase().contains(password.lowercase())) {
                    passwordEtSignUp.error = "Password cannot contain your name"
                    passwordEtSignUp.requestFocus()
                    return@setOnClickListener
                }
                if (charCount < 2) {
                    passwordEtSignUp.error = "Password should contain >=2 Uppercase characters"
                    passwordEtSignUp.requestFocus()
                    return@setOnClickListener
                }
                if (numberCount < 2) {
                    passwordEtSignUp.error = "Password should contain >=2 Digits"
                    passwordEtSignUp.requestFocus()
                    return@setOnClickListener
                }
                if (symbolCount < 1) {
                    passwordEtSignUp.error = "Password should contain >=1 Special Character"
                    passwordEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (confirmPass.isEmpty()) {
                    confirmPasswordEtSignUp.error = "Confirm Password required"
                    confirmPasswordEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (confirmPass != password) {
                    confirmPasswordEtSignUp.error = "Password Does not matches"
                    confirmPasswordEtSignUp.requestFocus()
                    return@setOnClickListener
                }

                if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty() && password == confirmPass) {
                    viewModel.insertUser(Users(name, email, phone, password))

                    Toast.makeText(this@SignUpActivity, "User Created", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}















