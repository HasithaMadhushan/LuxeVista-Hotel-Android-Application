package com.example.luxevista_hotel.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxevista_hotel.data.LuxeVistaRepository
import com.example.luxevista_hotel.data.User
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LuxeVistaRepository) : ViewModel() {

    // Function to get a user by email. This will be used to check if a user exists.
    fun getUserByEmail(email: String): LiveData<User> {
        return repository.getUserByEmail(email)
    }

    fun getUserByEmailAndPassword(email: String, password: String): LiveData<User> {
        return repository.getUserByEmailAndPassword(email, password)
    }

    // Function to register a new user. It runs in a background coroutine.
    fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            val newUser = User(firstName = firstName, lastName = lastName, email = email, password = password)
            repository.insertUser(newUser)
        }
    }
}