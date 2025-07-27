package com.example.luxevista_hotel.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.luxevista_hotel.data.Attraction
import com.example.luxevista_hotel.data.LuxeVistaRepository
import com.example.luxevista_hotel.data.Room
import com.example.luxevista_hotel.data.User
import kotlinx.coroutines.launch

class UserProfileViewModel(private val repository: LuxeVistaRepository) : ViewModel() {

    fun getUserProfile(userId: Int): LiveData<User> {
        return repository.getUserById(userId)
    }

    fun updateUserProfile(
        userId: Int,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        preferredRoomType: String,
        dietaryPreferences: String,
        specialRequests: String
    ) {
        viewModelScope.launch {
            // Fetch the current user directly from the database
            val currentUser = repository.getUserByIdNow(userId)
            currentUser?.let {
                val updatedUser = it.copy(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    preferredRoomType = preferredRoomType,
                    dietaryPreferences = dietaryPreferences,
                    specialRequests = specialRequests
                )
                repository.updateUser(updatedUser)
            }
        }
    }

    fun updatePhoneNumber(userId: Int, phoneNumber: String) {
        viewModelScope.launch {
            repository.updatePhoneNumber(userId, phoneNumber)
        }
    }

    fun updatePreferredRoomType(userId: Int, preferredRoomType: String) {
        viewModelScope.launch {
            repository.updatePreferredRoomType(userId, preferredRoomType)
        }
    }

    fun updateDietaryPreferences(userId: Int, dietaryPreferences: String) {
        viewModelScope.launch {
            repository.updateDietaryPreferences(userId, dietaryPreferences)
        }
    }

    fun updateSpecialRequests(userId: Int, specialRequests: String) {
        viewModelScope.launch {
            repository.updateSpecialRequests(userId, specialRequests)
        }
    }
    
    // Get personalized room recommendations based on user preferences
    fun getRecommendedRooms(userId: Int): LiveData<List<Room>> {
        val userPreferences = MutableLiveData<User>()
        
        // Get the user preferences
        repository.getUserById(userId).observeForever { user ->
            if (user != null) {
                userPreferences.value = user
            }
        }
        
        // Return rooms that match the user's preferred room type
        return userPreferences.map { user ->
            if (user != null && user.preferredRoomType.isNotEmpty()) {
                repository.getRoomsByType(user.preferredRoomType).value ?: emptyList()
            } else {
                repository.allRooms.value ?: emptyList()
            }
        }
    }
    
    // Get personalized attraction recommendations
    fun getRecommendedAttractions(userId: Int): LiveData<List<Attraction>> {
        return repository.getRecommendedAttractionsForUser(userId)
    }
} 