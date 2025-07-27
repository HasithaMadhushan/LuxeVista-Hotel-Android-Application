package com.example.luxevista_hotel.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.luxevista_hotel.R
import com.example.luxevista_hotel.data.Room

class RoomAdapter(private val onItemClicked: (Room) -> Unit) : ListAdapter<Room, RoomAdapter.RoomViewHolder>(RoomDiffCallback()) {

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomImageView: ImageView = itemView.findViewById(R.id.room_image)
        private val roomTypeTextView: TextView = itemView.findViewById(R.id.room_type_textview)
        private val roomDescriptionTextView: TextView = itemView.findViewById(R.id.room_description_textview)
        private val roomPriceTextView: TextView = itemView.findViewById(R.id.room_price_textview)
        private val viewDetailsButton: Button = itemView.findViewById(R.id.view_details_button)

        fun bind(room: Room, onItemClicked: (Room) -> Unit) {
            roomTypeTextView.text = room.type
            roomDescriptionTextView.text = room.description
            roomPriceTextView.text = "$${room.pricePerNight} / night"
            
            // Load local drawable images based on room type
            val imageResId = when (room.type) {
                "Ocean View Suite" -> R.drawable.ocean_view
                "Deluxe Room" -> R.drawable.deluxe_room
                "Garden View Room" -> R.drawable.garden_room
                "Presidential Suite" -> R.drawable.presidential_room
                "Family Suite" -> R.drawable.family_room
                "Honeymoon Suite" -> R.drawable.honeymoon_room
                "Standard Room" -> R.drawable.standerd_room
                "Penthouse" -> R.drawable.penthouse_room
                else -> R.drawable.ic_launcher_background // Default placeholder
            }
            Glide.with(itemView.context)
                .load(imageResId)
                .into(roomImageView)
            
            // Set text colors to ensure readability over potentially dark images
            roomTypeTextView.setTextColor(Color.WHITE)
            roomDescriptionTextView.setTextColor(Color.WHITE)
            roomPriceTextView.setTextColor(Color.YELLOW) // Use a contrasting color for price
            
            // Set click listeners
            viewDetailsButton.setOnClickListener {
                onItemClicked(room)
            }
            
            // Also set click listener on the whole item for better UX
            itemView.setOnClickListener {
                onItemClicked(room)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.room_list_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room, onItemClicked)
    }
}

// DiffCallback remains the same
class RoomDiffCallback : DiffUtil.ItemCallback<Room>() {
    override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
        return oldItem == newItem
    }
}