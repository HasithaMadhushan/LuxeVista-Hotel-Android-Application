package com.example.luxevista_hotel.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.databinding.ActivityRegisterBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountButton.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister() {
        val firstName = binding.firstNameEdittext.text.toString().trim()
        val lastName = binding.lastNameEdittext.text.toString().trim()
        val email = binding.emailEdittext.text.toString().trim()
        val password = binding.passwordEdittext.text.toString()
        val confirmPassword = binding.confirmPasswordEdittext.text.toString()
        var valid = true

        // Email format validation
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (firstName.isEmpty()) {
            binding.firstNameEdittext.error = "First name required"
            valid = false
        } else {
            binding.firstNameEdittext.error = null
        }
        if (lastName.isEmpty()) {
            binding.lastNameEdittext.error = "Last name required"
            valid = false
        } else {
            binding.lastNameEdittext.error = null
        }
        if (email.isEmpty()) {
            binding.emailEdittext.error = "Email required"
            valid = false
        } else if (!email.matches(emailPattern.toRegex())) {
            binding.emailEdittext.error = "Invalid email address"
            valid = false
        } else {
            binding.emailEdittext.error = null
        }
        if (password.length < 6) {
            binding.passwordEdittext.error = "Password must be at least 6 characters"
            valid = false
        } else {
            binding.passwordEdittext.error = null
        }
        if (password != confirmPassword) {
            binding.confirmPasswordEdittext.error = "Passwords do not match"
            valid = false
        } else {
            binding.confirmPasswordEdittext.error = null
        }
        if (!valid) return

        // Check if user already exists
        loginViewModel.getUserByEmail(email).observe(this) { existingUser ->
            if (existingUser == null) {
                // User does not exist, proceed with registration
                loginViewModel.registerUser(firstName, lastName, email, password)
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                finish() // Close the register activity and return to login
            } else {
                // User already exists
                Toast.makeText(this, "This email is already registered.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}