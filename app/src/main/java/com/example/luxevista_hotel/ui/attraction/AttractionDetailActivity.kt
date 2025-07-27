package com.example.luxevista_hotel.ui.attraction

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.databinding.ActivityAttractionDetailBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory

class AttractionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttractionDetailBinding
    private val attractionViewModel: AttractionViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttractionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val attractionId = intent.getIntExtra("ATTRACTION_ID", -1)

        if (attractionId != -1) {
            loadAttractionDetails(attractionId)
            loadSimilarAttractions(attractionId)
        } else {
            finish()
        }
    }

    private fun loadAttractionDetails(attractionId: Int) {
        attractionViewModel.getAttractionById(attractionId).observe(this) { attraction ->
            attraction?.let {
                binding.attractionNameText.text = it.name
                binding.attractionTypeText.text = it.type
                binding.attractionDescriptionText.text = it.description
                
                // If it's a hotel offer, show the "Book Now" button
                if (it.type == "Hotel Offer") {
                    binding.bookNowButton.visibility = View.VISIBLE
                    binding.bookNowButton.setOnClickListener { _ ->
                        // In a real app, this would navigate to a booking screen
                        // or add the offer to the user's reservation
                    }
                } else {
                    binding.bookNowButton.visibility = View.GONE
                }
            }
        }
    }

    private fun loadSimilarAttractions(attractionId: Int) {
        attractionViewModel.getSimilarAttractions(attractionId).observe(this) { attractions ->
            if (attractions.isNotEmpty()) {
                binding.similarAttractionsLayout.visibility = View.VISIBLE
                
                val adapter = RecommendedAttractionsAdapter(this)
                binding.similarAttractionsRecyclerView.adapter = adapter
                adapter.submitList(attractions)
            } else {
                binding.similarAttractionsLayout.visibility = View.GONE
            }
        }
    }
} 