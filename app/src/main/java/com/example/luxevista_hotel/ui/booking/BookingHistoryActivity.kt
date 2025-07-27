package com.example.luxevista_hotel.ui.booking

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.databinding.ActivityBookingHistoryBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory
import com.example.luxevista_hotel.util.SessionManager
import com.google.android.material.tabs.TabLayout

class BookingHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingHistoryBinding
    private val bookingViewModel: BookingViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    // In a real app, you would get the user ID from SharedPreferences or a session manager
    private var currentUserId: Int = -1

    private lateinit var bookingAdapter: BookingHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID from session
        currentUserId = SessionManager.getUserId(this)
        setupRecyclerView()
        setupTabLayout()
        observeCancellationResult()

        // By default, show upcoming bookings
        loadUpcomingBookings()
    }

    private fun setupRecyclerView() {
        bookingAdapter = BookingHistoryAdapter(this) { bookingId ->
            showCancellationConfirmationDialog(bookingId)
        }
        binding.bookingsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@BookingHistoryActivity)
            adapter = bookingAdapter
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadUpcomingBookings()
                    1 -> loadPastBookings()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadUpcomingBookings() {
        bookingViewModel.getUpcomingBookings(currentUserId).observe(this) { bookings ->
            bookingAdapter.submitList(bookings)
            
            // Show empty state if no bookings
            if (bookings.isEmpty()) {
                binding.emptyStateText.text = "No upcoming bookings"
                binding.emptyStateLayout.visibility = android.view.View.VISIBLE
            } else {
                binding.emptyStateLayout.visibility = android.view.View.GONE
            }
        }
    }

    private fun loadPastBookings() {
        bookingViewModel.getPastBookings(currentUserId).observe(this) { bookings ->
            bookingAdapter.submitList(bookings)
            
            // Show empty state if no bookings
            if (bookings.isEmpty()) {
                binding.emptyStateText.text = "No past bookings"
                binding.emptyStateLayout.visibility = android.view.View.VISIBLE
            } else {
                binding.emptyStateLayout.visibility = android.view.View.GONE
            }
        }
    }
    
    private fun showCancellationConfirmationDialog(bookingId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Cancel Booking")
            .setMessage("Are you sure you want to cancel this booking?")
            .setPositiveButton("Yes") { _, _ ->
                bookingViewModel.cancelBooking(bookingId)
            }
            .setNegativeButton("No", null)
            .show()
    }
    
    private fun observeCancellationResult() {
        bookingViewModel.cancellationResult.observe(this) { result ->
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            
            if (result.success) {
                // Refresh the list to show updated status
                loadUpcomingBookings()
            }
        }
    }
} 