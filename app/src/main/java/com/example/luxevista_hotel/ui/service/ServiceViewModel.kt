package com.example.luxevista_hotel.ui.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxevista_hotel.data.LuxeVistaRepository
import com.example.luxevista_hotel.data.Service
import com.example.luxevista_hotel.ui.booking.BookingResult
import kotlinx.coroutines.launch
import java.util.Calendar

class ServiceViewModel(private val repository: LuxeVistaRepository) : ViewModel() {

    private val _bookingResult = MutableLiveData<BookingResult>()
    val bookingResult: LiveData<BookingResult> = _bookingResult

    // Get all services
    val allServices: LiveData<List<Service>> = repository.allServices

    // Get services sorted by price
    fun getServicesSortedByPrice(ascending: Boolean): LiveData<List<Service>> {
        return if (ascending) {
            repository.getServicesSortedByPriceAsc()
        } else {
            repository.getServicesSortedByPriceDesc()
        }
    }

    // Search services by name
    fun searchServices(query: String): LiveData<List<Service>> {
        return repository.searchServices(query)
    }

    // Get service details
    fun getServiceDetails(serviceId: Int): LiveData<Service> {
        return repository.getServiceById(serviceId)
    }

    // Check service availability for a specific date
    fun checkServiceAvailability(serviceId: Int, date: Long) {
        viewModelScope.launch {
            val isAvailable = repository.isServiceAvailable(serviceId, date)
            _bookingResult.value = BookingResult(
                isAvailable,
                if (isAvailable) "Service is available on the selected date" else "Service is not available on the selected date"
            )
        }
    }

    // Book a service
    fun bookService(userId: Int, serviceId: Int, date: Long, specialRequests: String, servicePrice: Double) {
        viewModelScope.launch {
            // First check if the service is available
            val isAvailable = repository.isServiceAvailable(serviceId, date)

            if (!isAvailable) {
                _bookingResult.value = BookingResult(false, "Service is not available on the selected date")
                return@launch
            }

            try {
                val bookingId = repository.bookService(userId, serviceId, date, specialRequests, servicePrice)
                
                // Add loyalty points (5 points per service booking)
                repository.addLoyaltyPoints(userId, 5)
                
                _bookingResult.value = BookingResult(true, "Service booked successfully", bookingId.toInt())
            } catch (e: Exception) {
                _bookingResult.value = BookingResult(false, "Error booking service: ${e.message}")
            }
        }
    }
} 