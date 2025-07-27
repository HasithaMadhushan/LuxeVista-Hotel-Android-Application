package com.example.luxevista_hotel.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.luxevista_hotel.data.LuxeVistaRepository
import com.example.luxevista_hotel.data.Room

class DetailViewModel(private val repository: LuxeVistaRepository) : ViewModel() {

    fun getRoomDetails(roomId: Int): LiveData<Room> {
        return repository.getRoomById(roomId)
    }
}