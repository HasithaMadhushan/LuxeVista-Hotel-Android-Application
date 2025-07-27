package com.example.luxevista_hotel.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.MainActivity
import com.example.luxevista_hotel.databinding.ActivityLoginBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory
import com.example.luxevista_hotel.util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            handleLogin()
        }

        // Updated to open the RegisterActivity
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {
        val email = binding.emailEdittext.text.toString().trim()
        val password = binding.passwordEdittext.text.toString()
        var valid = true

        // Email format validation
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isEmpty()) {
            binding.emailEdittext.error = "Email required"
            valid = false
        } else if (!email.matches(emailPattern.toRegex())) {
            binding.emailEdittext.error = "Invalid email address"
            valid = false
        } else {
            binding.emailEdittext.error = null
        }

        if (password.isEmpty()) {
            binding.passwordEdittext.error = "Password required"
            valid = false
        } else {
            binding.passwordEdittext.error = null
        }

        if (!valid) return

        // Check both email and password
        loginViewModel.getUserByEmailAndPassword(email, password).observe(this) { user ->
            loginViewModel.getUserByEmailAndPassword(email, password).removeObservers(this)

            if (user != null) {
                // Save user ID in session
                SessionManager.saveUserId(this, user.id)
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}