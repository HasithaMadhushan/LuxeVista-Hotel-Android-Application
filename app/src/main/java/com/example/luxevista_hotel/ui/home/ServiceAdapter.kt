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
import com.example.luxevista_hotel.data.Service

class ServiceAdapter(private val onItemClicked: (Service) -> Unit) : ListAdapter<Service, ServiceAdapter.ServiceViewHolder>(ServiceDiffCallback()) {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceImageView: ImageView = itemView.findViewById(R.id.service_image)
        private val serviceNameTextView: TextView = itemView.findViewById(R.id.service_name_textview)
        private val servicePriceTextView: TextView = itemView.findViewById(R.id.service_price_textview)
        private val bookServiceButton: Button = itemView.findViewById(R.id.book_service_button)

        fun bind(service: Service, onItemClicked: (Service) -> Unit) {
            serviceNameTextView.text = service.name
            servicePriceTextView.text = "$${service.price}"
            
            // Map service name to drawable image
            val imageResId = when (service.name) {
                "Deep Tissue Massage" -> R.drawable.deep_tissue_massage
                "Fine Dining Reservation" -> R.drawable.fine_dining
                "Poolside Cabana" -> R.drawable.pool_cabana
                "Sunset Yacht Tour" -> R.drawable.sunset_yacht_tour
                "Private Beach Dinner" -> R.drawable.private_beach_dinner
                "Spa Day Package" -> R.drawable.spa_day_package
                "Golf Course Access" -> R.drawable.golf_course
                "Tennis Court Rental" -> R.drawable.tennis_court_rental
                else -> R.drawable.ic_launcher_background
            }
            Glide.with(itemView.context)
                .load(imageResId)
                .centerCrop()
                .into(serviceImageView)
            
            // Set click listeners
            bookServiceButton.setOnClickListener {
                onItemClicked(service)
            }
            itemView.setOnClickListener {
                onItemClicked(service)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_list_item, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = getItem(position)
        holder.bind(service, onItemClicked)
    }
}

class ServiceDiffCallback : DiffUtil.ItemCallback<Service>() {
    override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
        return oldItem == newItem
    }
}