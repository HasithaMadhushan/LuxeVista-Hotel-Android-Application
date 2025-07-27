package com.example.luxevista_hotel.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.databinding.ActivityUserProfileBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory
import com.example.luxevista_hotel.ui.attraction.RecommendedAttractionsAdapter
import com.example.luxevista_hotel.ui.home.RoomAdapter
import com.example.luxevista_hotel.ui.detail.RoomDetailActivity
import com.example.luxevista_hotel.util.SessionManager

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val profileViewModel: UserProfileViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    // In a real app, you would get the user ID from SharedPreferences or a session manager
    // For now, we'll use a hardcoded value for demonstration
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID from session
        currentUserId = SessionManager.getUserId(this)

        // Load user profile data
        profileViewModel.getUserProfile(currentUserId).observe(this) { user ->
            user?.let {
                binding.profileFirstNameEditText.setText(it.firstName)
                binding.profileLastNameEditText.setText(it.lastName)
                binding.profileEmailEditText.setText(it.email)
                binding.profilePhoneEditText.setText(it.phoneNumber)
                binding.preferredRoomTypeEditText.setText(it.preferredRoomType)
                binding.dietaryPreferencesEditText.setText(it.dietaryPreferences)
                binding.specialRequestsEditText.setText(it.specialRequests)
                binding.loyaltyPointsText.text = "Loyalty Points: ${it.loyaltyPoints}"
                
                // Show personalized recommendations if preferences are set
                if (it.preferredRoomType.isNotEmpty()) {
                    binding.recommendationsLayout.visibility = View.VISIBLE
                    loadRecommendations()
                } else {
                    binding.recommendationsLayout.visibility = View.GONE
                }
            }
        }

        // Set up save button
        binding.saveProfileButton.setOnClickListener {
            saveUserProfile()
        }
        
        // Set up recommended rooms recycler view
        val roomAdapter = RoomAdapter { room ->
            val intent = Intent(this, RoomDetailActivity::class.java)
            intent.putExtra("ROOM_ID", room.id)
            startActivity(intent)
        }
        binding.recommendedRoomsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedRoomsRecyclerView.adapter = roomAdapter
        
        // Set up recommended attractions recycler view
        val attractionAdapter = RecommendedAttractionsAdapter(this)
        binding.recommendedAttractionsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedAttractionsRecyclerView.adapter = attractionAdapter
        
        // Load recommendations
        profileViewModel.getRecommendedRooms(currentUserId).observe(this) { rooms ->
            if (rooms.isNotEmpty()) {
                binding.recommendedRoomsLayout.visibility = View.VISIBLE
                roomAdapter.submitList(rooms)
            } else {
                binding.recommendedRoomsLayout.visibility = View.GONE
            }
        }
        
        profileViewModel.getRecommendedAttractions(currentUserId).observe(this) { attractions ->
            if (attractions.isNotEmpty()) {
                binding.recommendedAttractionsLayout.visibility = View.VISIBLE
                attractionAdapter.submitList(attractions)
            } else {
                binding.recommendedAttractionsLayout.visibility = View.GONE
            }
        }
    }

    private fun saveUserProfile() {
        val firstName = binding.profileFirstNameEditText.text.toString().trim()
        val lastName = binding.profileLastNameEditText.text.toString().trim()
        val phoneNumber = binding.profilePhoneEditText.text.toString().trim()
        val preferredRoomType = binding.preferredRoomTypeEditText.text.toString().trim()
        val dietaryPreferences = binding.dietaryPreferencesEditText.text.toString().trim()
        val specialRequests = binding.specialRequestsEditText.text.toString().trim()

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Update profile
        profileViewModel.updateUserProfile(
            currentUserId,
            firstName,
            lastName,
            phoneNumber,
            preferredRoomType,
            dietaryPreferences,
            specialRequests
        )

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        
        // Show recommendations section if preferences are set
        if (preferredRoomType.isNotEmpty()) {
            binding.recommendationsLayout.visibility = View.VISIBLE
            loadRecommendations()
        }
    }
    
    private fun loadRecommendations() {
        // This will trigger the observers we set up in onCreate
        profileViewModel.getRecommendedRooms(currentUserId)
        profileViewModel.getRecommendedAttractions(currentUserId)
    }
} 