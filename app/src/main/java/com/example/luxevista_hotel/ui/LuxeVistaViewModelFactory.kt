package com.example.luxevista_hotel.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.luxevista_hotel.data.LuxeVistaRepository
import com.example.luxevista_hotel.ui.attraction.AttractionViewModel
import com.example.luxevista_hotel.ui.auth.LoginViewModel
import com.example.luxevista_hotel.ui.booking.BookingViewModel
import com.example.luxevista_hotel.ui.detail.DetailViewModel // Import was missing
import com.example.luxevista_hotel.ui.home.HomeViewModel
import com.example.luxevista_hotel.ui.profile.UserProfileViewModel
import com.example.luxevista_hotel.ui.service.ServiceViewModel

class LuxeVistaViewModelFactory(private val repository: LuxeVistaRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        // Add check for DetailViewModel
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        // Add check for UserProfileViewModel
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(repository) as T
        }
        // Add check for BookingViewModel
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(repository) as T
        }
        // Add check for ServiceViewModel
        if (modelClass.isAssignableFrom(ServiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceViewModel(repository) as T
        }
        // Add check for AttractionViewModel
        if (modelClass.isAssignableFrom(AttractionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AttractionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}