package com.example.luxevista_hotel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.luxevista_hotel.databinding.ActivityMainBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory
import com.example.luxevista_hotel.ui.home.AttractionAdapter
import com.example.luxevista_hotel.ui.home.HomeViewModel
import com.example.luxevista_hotel.ui.home.RoomAdapter
import com.example.luxevista_hotel.ui.home.ServiceAdapter
import com.example.luxevista_hotel.data.Room
import com.example.luxevista_hotel.data.Service
import com.example.luxevista_hotel.data.Attraction
import com.example.luxevista_hotel.ui.attraction.AttractionDetailActivity
import com.example.luxevista_hotel.ui.booking.BookingHistoryActivity
import com.example.luxevista_hotel.ui.detail.RoomDetailActivity
import com.example.luxevista_hotel.ui.profile.UserProfileActivity
import com.example.luxevista_hotel.ui.service.ServiceDetailActivity
import com.example.luxevista_hotel.ui.auth.LoginActivity
import com.example.luxevista_hotel.util.SessionManager
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeViewModel: HomeViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    private lateinit var roomAdapter: RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up profile button
        binding.profileButton.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
        
        // Set up bookings button
        binding.bookingsButton.setOnClickListener {
            val intent = Intent(this, BookingHistoryActivity::class.java)
            startActivity(intent)
        }

        // Set up logout button
        binding.logoutButton.setOnClickListener {
            // In a real app, you would clear user session/preferences here
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Set up filter button
        binding.filterButton.setOnClickListener {
            showFilterDialog()
        }

        // Pass the click handler lambda to the RoomAdapter constructor
        roomAdapter = RoomAdapter { room ->
            onRoomClicked(room)
        }
        binding.roomsRecyclerview.adapter = roomAdapter

        val serviceAdapter = ServiceAdapter { service ->
            onServiceClicked(service)
        }
        binding.servicesRecyclerview.adapter = serviceAdapter

        val attractionAdapter = AttractionAdapter { attraction ->
            onAttractionClicked(attraction)
        }
        binding.attractionsRecyclerview.adapter = attractionAdapter

        // --- Observe LiveData from the ViewModel ---
        homeViewModel.allRooms.observe(this) { rooms ->
            roomAdapter.submitList(rooms)
        }

        // Observe filtered rooms
        homeViewModel.filteredRooms.observe(this) { filteredRooms ->
            if (filteredRooms != null && filteredRooms.isNotEmpty()) {
                roomAdapter.submitList(filteredRooms)
            }
        }

        homeViewModel.allServices.observe(this) { services ->
            serviceAdapter.submitList(services)
        }

        homeViewModel.allAttractions.observe(this) { attractions ->
            attractionAdapter.submitList(attractions)
        }
    }

    // This function will be called when a room is clicked
    private fun onRoomClicked(room: Room) {
        val intent = Intent(this, RoomDetailActivity::class.java)
        // Pass the ID of the clicked room to the detail activity
        intent.putExtra("ROOM_ID", room.id)
        startActivity(intent)
    }
    
    // This function will be called when a service is clicked
    private fun onServiceClicked(service: Service) {
        val intent = Intent(this, ServiceDetailActivity::class.java)
        // Pass the ID of the clicked service to the detail activity
        intent.putExtra("SERVICE_ID", service.id)
        startActivity(intent)
    }
    
    // This function will be called when an attraction is clicked
    private fun onAttractionClicked(attraction: Attraction) {
        val intent = Intent(this, AttractionDetailActivity::class.java)
        // Pass the ID of the clicked attraction to the detail activity
        intent.putExtra("ATTRACTION_ID", attraction.id)
        startActivity(intent)
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.room_filter_layout, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Get references to the filter views
        val roomTypeFilter = dialogView.findViewById<TextInputEditText>(R.id.room_type_filter)
        val minPriceFilter = dialogView.findViewById<TextInputEditText>(R.id.min_price_filter)
        val maxPriceFilter = dialogView.findViewById<TextInputEditText>(R.id.max_price_filter)
        val sortPriceAsc = dialogView.findViewById<RadioButton>(R.id.sort_price_asc)
        val applyButton = dialogView.findViewById<Button>(R.id.apply_filters_button)
        val resetButton = dialogView.findViewById<Button>(R.id.reset_filters_button)

        // Set up apply button
        applyButton.setOnClickListener {
            val roomType = roomTypeFilter.text.toString().trim()
            val minPrice = minPriceFilter.text.toString().toDoubleOrNull() ?: 0.0
            val maxPrice = maxPriceFilter.text.toString().toDoubleOrNull() ?: Double.MAX_VALUE
            
            // Apply filters
            homeViewModel.applyFilters(roomType, minPrice, maxPrice)
            
            // Apply sorting
            homeViewModel.sortByPrice(sortPriceAsc.isChecked)
            
            dialog.dismiss()
            Toast.makeText(this, "Filters applied", Toast.LENGTH_SHORT).show()
        }

        // Set up reset button
        resetButton.setOnClickListener {
            homeViewModel.resetFilters()
            dialog.dismiss()
            Toast.makeText(this, "Filters reset", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}