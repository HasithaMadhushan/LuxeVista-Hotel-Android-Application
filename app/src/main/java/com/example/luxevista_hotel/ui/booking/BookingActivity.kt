package com.example.luxevista_hotel.ui.booking

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.databinding.ActivityBookingBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory
import com.example.luxevista_hotel.util.SessionManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private val bookingViewModel: BookingViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    // In a real app, you would get the user ID from SharedPreferences or a session manager
    private var currentUserId: Int = -1
    private var roomId: Int = -1
    private var roomPrice: Double = 0.0
    
    private var checkInDate: Calendar = Calendar.getInstance()
    private var checkOutDate: Calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID from session
        currentUserId = SessionManager.getUserId(this)

        // Get room ID from intent
        roomId = intent.getIntExtra("ROOM_ID", -1)
        
        if (roomId == -1) {
            Toast.makeText(this, "Error: Room not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set up date pickers
        binding.checkInDateButton.setOnClickListener {
            showDatePicker(true)
        }

        binding.checkOutDateButton.setOnClickListener {
            showDatePicker(false)
        }

        // Load room details
        bookingViewModel.getRoomDetails(roomId).observe(this) { room ->
            room?.let {
                binding.bookingRoomType.text = it.type
                binding.bookingRoomDescription.text = it.description
                binding.bookingRoomPrice.text = "$${it.pricePerNight} / night"
                roomPrice = it.pricePerNight
                updateTotalPrice()
            }
        }

        // Set up booking button
        binding.confirmBookingButton.setOnClickListener {
            createBooking()
        }

        // Set up availability check button
        binding.checkAvailabilityButton.setOnClickListener {
            checkAvailability()
        }

        // Observe booking results
        bookingViewModel.bookingResult.observe(this) { result ->
            Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            
            if (result.success && result.bookingId != -1) {
                // Booking was successful, finish activity
                finish()
            }
        }

        // Initialize date display
        updateDateDisplay()
    }

    private fun showDatePicker(isCheckIn: Boolean) {
        val calendar = if (isCheckIn) checkInDate else checkOutDate
        
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                
                // If check-in date is after check-out date, update check-out date
                if (isCheckIn && checkInDate.timeInMillis >= checkOutDate.timeInMillis) {
                    checkOutDate.timeInMillis = checkInDate.timeInMillis
                    checkOutDate.add(Calendar.DAY_OF_MONTH, 1)
                }
                
                updateDateDisplay()
                updateTotalPrice()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Set min date to today for check-in, or check-in date for check-out
            if (isCheckIn) {
                datePicker.minDate = System.currentTimeMillis() - 1000
            } else {
                datePicker.minDate = checkInDate.timeInMillis + 24 * 60 * 60 * 1000
            }
        }.show()
    }

    private fun updateDateDisplay() {
        binding.checkInDateText.text = dateFormat.format(checkInDate.time)
        binding.checkOutDateText.text = dateFormat.format(checkOutDate.time)
    }

    private fun updateTotalPrice() {
        val nights = calculateNights(checkInDate.timeInMillis, checkOutDate.timeInMillis)
        val totalPrice = roomPrice * nights
        binding.totalPriceText.text = "Total: $${String.format("%.2f", totalPrice)} for $nights night(s)"
    }

    private fun calculateNights(checkIn: Long, checkOut: Long): Long {
        val millisecondsPerDay = 24 * 60 * 60 * 1000L
        return (checkOut - checkIn) / millisecondsPerDay
    }

    private fun checkAvailability() {
        bookingViewModel.checkRoomAvailability(
            roomId,
            checkInDate.timeInMillis,
            checkOutDate.timeInMillis
        )
    }

    private fun createBooking() {
        val guestsText = binding.numberOfGuestsInput.text.toString()
        val numberOfGuests = if (guestsText.isNotEmpty()) guestsText.toInt() else 1
        val specialRequests = binding.specialRequestsInput.text.toString().trim()

        bookingViewModel.createBooking(
            currentUserId,
            roomId,
            checkInDate.timeInMillis,
            checkOutDate.timeInMillis,
            numberOfGuests,
            specialRequests,
            roomPrice
        )
    }
} 