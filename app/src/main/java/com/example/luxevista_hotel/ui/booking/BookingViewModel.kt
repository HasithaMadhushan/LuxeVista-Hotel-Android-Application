package com.example.luxevista_hotel.ui.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luxevista_hotel.data.Booking
import com.example.luxevista_hotel.data.LuxeVistaRepository
import com.example.luxevista_hotel.data.Room
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class BookingViewModel(private val repository: LuxeVistaRepository) : ViewModel() {

    private val _bookingResult = MutableLiveData<BookingResult>()
    val bookingResult: LiveData<BookingResult> = _bookingResult
    
    private val _cancellationResult = MutableLiveData<CancellationResult>()
    val cancellationResult: LiveData<CancellationResult> = _cancellationResult

    fun getRoomDetails(roomId: Int): LiveData<Room> {
        return repository.getRoomById(roomId)
    }

    fun checkRoomAvailability(roomId: Int, checkInDate: Long, checkOutDate: Long) {
        viewModelScope.launch {
            val isAvailable = repository.isRoomAvailable(roomId, checkInDate, checkOutDate)
            _bookingResult.value = BookingResult(isAvailable, if (isAvailable) "Room is available" else "Room is not available for the selected dates")
        }
    }

    fun createBooking(
        userId: Int,
        roomId: Int,
        checkInDate: Long,
        checkOutDate: Long,
        numberOfGuests: Int,
        specialRequests: String,
        roomPrice: Double
    ) {
        viewModelScope.launch {
            // First check if the room is available
            val isAvailable = repository.isRoomAvailable(roomId, checkInDate, checkOutDate)
            
            if (!isAvailable) {
                _bookingResult.value = BookingResult(false, "Room is not available for the selected dates")
                return@launch
            }
            
            // Calculate number of nights
            val nights = calculateNights(checkInDate, checkOutDate)
            
            // Calculate total price
            val totalPrice = roomPrice * nights
            
            // Create booking
            val booking = Booking(
                userId = userId,
                roomId = roomId,
                serviceId = null,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                numberOfGuests = numberOfGuests,
                specialRequests = specialRequests,
                totalPrice = totalPrice
            )
            
            try {
                val bookingId = repository.insertBooking(booking)
                
                // Add loyalty points (10 points per night)
                repository.addLoyaltyPoints(userId, nights.toInt() * 10)
                
                _bookingResult.value = BookingResult(true, "Booking confirmed successfully", bookingId.toInt())
            } catch (e: Exception) {
                _bookingResult.value = BookingResult(false, "Error creating booking: ${e.message}")
            }
        }
    }
    
    fun cancelBooking(bookingId: Int) {
        viewModelScope.launch {
            try {
                repository.updateBookingStatus(bookingId, "Cancelled")
                _cancellationResult.value = CancellationResult(true, "Booking cancelled successfully")
            } catch (e: Exception) {
                _cancellationResult.value = CancellationResult(false, "Error cancelling booking: ${e.message}")
            }
        }
    }
    
    fun getUpcomingBookings(userId: Int): LiveData<List<Booking>> {
        return repository.getUpcomingBookingsForUser(userId)
    }
    
    fun getPastBookings(userId: Int): LiveData<List<Booking>> {
        return repository.getPastBookingsForUser(userId)
    }
    
    private fun calculateNights(checkInDate: Long, checkOutDate: Long): Long {
        val millisecondsPerDay = 24 * 60 * 60 * 1000L
        return (checkOutDate - checkInDate) / millisecondsPerDay
    }
}

data class BookingResult(
    val success: Boolean,
    val message: String,
    val bookingId: Int = -1
)

data class CancellationResult(
    val success: Boolean,
    val message: String
) 