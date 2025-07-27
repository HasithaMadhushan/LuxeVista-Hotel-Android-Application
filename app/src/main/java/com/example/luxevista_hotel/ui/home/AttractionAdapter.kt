package com.example.luxevista_hotel.ui.home

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
import com.example.luxevista_hotel.data.Attraction
import com.example.luxevista_hotel.ui.attraction.AttractionDetailActivity

class AttractionAdapter(private val onItemClicked: ((Attraction) -> Unit)? = null) : 
    ListAdapter<Attraction, AttractionAdapter.AttractionViewHolder>(AttractionDiffCallback()) {

    class AttractionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val attractionImageView: ImageView = itemView.findViewById(R.id.attraction_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.attraction_name_textview)
        private val typeTextView: TextView = itemView.findViewById(R.id.attraction_type_textview)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.attraction_description_textview)
        private val viewDetailsButton: Button = itemView.findViewById(R.id.view_attraction_button)

        fun bind(attraction: Attraction, onItemClicked: ((Attraction) -> Unit)?) {
            nameTextView.text = attraction.name
            typeTextView.text = attraction.type
            descriptionTextView.text = attraction.description
            
            // Map attraction name to drawable image
            val imageResId = when (attraction.name) {
                "Sunset Beach Tour" -> R.drawable.sunset_beach_tour
                "20% Off Spa Treatments"-> R.drawable.spa_treatment
                "Coral Reef Diving" -> R.drawable.coral_reef_diving
                "Local Cuisine Tour" -> R.drawable.local_cuisine_tour
                "Free Welcome Drink" -> R.drawable.free_welcome_drink
                "Historical City Tour" -> R.drawable.historical_city_tour

                else -> R.drawable.ic_launcher_background
            }
            Glide.with(itemView.context)
                .load(imageResId)
                .centerCrop()
                .into(attractionImageView)
            
            // Set click listeners
            viewDetailsButton.setOnClickListener {
                handleClick(attraction, onItemClicked)
            }
            itemView.setOnClickListener {
                handleClick(attraction, onItemClicked)
            }
        }
        
        private fun handleClick(attraction: Attraction, onItemClicked: ((Attraction) -> Unit)?) {
            onItemClicked?.invoke(attraction) ?: run {
                val context = itemView.context
                val intent = android.content.Intent(context, AttractionDetailActivity::class.java)
                intent.putExtra("ATTRACTION_ID", attraction.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.attraction_list_item, parent, false)
        return AttractionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val attraction = getItem(position)
        holder.bind(attraction, onItemClicked)
    }
}

class AttractionDiffCallback : DiffUtil.ItemCallback<Attraction>() {
    override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
        return oldItem == newItem
    }
}