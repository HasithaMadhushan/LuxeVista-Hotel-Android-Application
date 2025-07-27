package com.example.luxevista_hotel.ui.home // <-- Verify this line

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.luxevista_hotel.data.Attraction
import com.example.luxevista_hotel.data.LuxeVistaRepository
import com.example.luxevista_hotel.data.Room
import com.example.luxevista_hotel.data.Service

class HomeViewModel(private val repository: LuxeVistaRepository) : ViewModel() {

    // Filter parameters
    private val _roomTypeFilter = MutableLiveData<String>("")
    private val _minPriceFilter = MutableLiveData<Double>(0.0)
    private val _maxPriceFilter = MutableLiveData<Double>(Double.MAX_VALUE)
    private val _sortByPriceAsc = MutableLiveData<Boolean>(true)
    
    // Room list that changes based on filters
    val filteredRooms = MutableLiveData<List<Room>>()
    
    // Default room list
    val allRooms: LiveData<List<Room>> = repository.allRooms
    val allServices: LiveData<List<Service>> = repository.allServices
    val allAttractions: LiveData<List<Attraction>> = repository.allAttractions
    
    // Apply filters
    fun applyFilters(roomType: String, minPrice: Double, maxPrice: Double) {
        _roomTypeFilter.value = roomType
        _minPriceFilter.value = minPrice
        _maxPriceFilter.value = maxPrice
        
        // Apply the filters
        repository.filterRooms(roomType, minPrice, maxPrice).observeForever { rooms ->
            filteredRooms.value = rooms
        }
    }
    
    // Sort by price
    fun sortByPrice(ascending: Boolean) {
        _sortByPriceAsc.value = ascending
        
        if (ascending) {
            repository.getRoomsSortedByPriceAsc().observeForever { rooms ->
                filteredRooms.value = rooms
            }
        } else {
            repository.getRoomsSortedByPriceDesc().observeForever { rooms ->
                filteredRooms.value = rooms
            }
        }
    }
    
    // Reset filters
    fun resetFilters() {
        _roomTypeFilter.value = ""
        _minPriceFilter.value = 0.0
        _maxPriceFilter.value = Double.MAX_VALUE
        _sortByPriceAsc.value = true
        
        // Reset to default list
        repository.allRooms.observeForever { rooms ->
            filteredRooms.value = rooms
        }
    }
}