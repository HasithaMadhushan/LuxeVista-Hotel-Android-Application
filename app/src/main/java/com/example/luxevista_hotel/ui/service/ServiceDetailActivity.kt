package com.example.luxevista_hotel.ui.service

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.databinding.ActivityServiceDetailBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory
import com.example.luxevista_hotel.util.SessionManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ServiceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceDetailBinding
    private val serviceViewModel: ServiceViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    // In a real app, you would get the user ID from SharedPreferences or a session manager
    private var currentUserId: Int = -1
    private var serviceId: Int = -1
    private var servicePrice: Double = 0.0
    
    private var selectedDate: Calendar = Calendar.getInstance().apply { 
        add(Calendar.DAY_OF_MONTH, 1) // Default to tomorrow
    }
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID from session
        currentUserId = SessionManager.getUserId(this)

        // Get service ID from intent
        serviceId = intent.getIntExtra("SERVICE_ID", -1)
        
        if (serviceId == -1) {
            Toast.makeText(this, "Error: Service not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set up date picker
        binding.selectDateButton.setOnClickListener {
            showDatePicker()
        }

        // Load service details
        serviceViewModel.getServiceDetails(serviceId).observe(this) { service ->
            service?.let {
                binding.serviceNameText.text = it.name
                binding.serviceDescriptionText.text = it.description
                binding.servicePriceText.text = "$${it.price}"
                servicePrice = it.price
            }
        }

        // Set up booking button
        binding.bookServiceButton.setOnClickListener {
            bookService()
        }

        // Set up availability check button
        binding.checkAvailabilityButton.setOnClickListener {
            checkAvailability()
        }

        // Observe booking results
        serviceViewModel.bookingResult.observe(this) { result ->
            Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
            
            if (result.success && result.bookingId != -1) {
                // Booking was successful, finish activity
                finish()
            }
        }

        // Initialize date display
        updateDateDisplay()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateDisplay()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Set min date to tomorrow
            val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
            datePicker.minDate = tomorrow.timeInMillis
        }.show()
    }

    private fun updateDateDisplay() {
        binding.selectedDateText.text = dateFormat.format(selectedDate.time)
    }

    private fun checkAvailability() {
        serviceViewModel.checkServiceAvailability(serviceId, selectedDate.timeInMillis)
    }

    private fun bookService() {
        val specialRequests = binding.specialRequestsInput.text.toString().trim()

        serviceViewModel.bookService(
            currentUserId,
            serviceId,
            selectedDate.timeInMillis,
            specialRequests,
            servicePrice
        )
    }
} 