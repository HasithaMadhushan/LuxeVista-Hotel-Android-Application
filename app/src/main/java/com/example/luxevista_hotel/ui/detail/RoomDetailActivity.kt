package com.example.luxevista_hotel.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.luxevista_hotel.LuxeVistaApplication
import com.example.luxevista_hotel.databinding.ActivityRoomDetailBinding
import com.example.luxevista_hotel.ui.LuxeVistaViewModelFactory
import com.example.luxevista_hotel.ui.booking.BookingActivity

class RoomDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        LuxeVistaViewModelFactory((application as LuxeVistaApplication).repository)
    }

    private var roomId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomId = intent.getIntExtra("ROOM_ID", -1)

        if (roomId != -1) {
            detailViewModel.getRoomDetails(roomId).observe(this) { room ->
                room?.let {
                    binding.detailRoomType.text = it.type
                    binding.detailRoomDescription.text = it.description
                    binding.detailRoomPrice.text = "$${it.pricePerNight} / night"
                }
            }
        }

        binding.bookNowButton.setOnClickListener {
            if (roomId != -1) {
                val intent = Intent(this, BookingActivity::class.java)
                intent.putExtra("ROOM_ID", roomId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error: Room not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}