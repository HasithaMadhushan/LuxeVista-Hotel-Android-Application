package com.example.luxevista_hotel.ui.attraction

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.luxevista_hotel.R
import com.example.luxevista_hotel.data.Attraction

class RecommendedAttractionsAdapter(private val context: Context) : 
    ListAdapter<Attraction, RecommendedAttractionsAdapter.AttractionViewHolder>(AttractionDiffCallback()) {

    class AttractionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.recommended_attraction_name)
        val typeTextView: TextView = view.findViewById(R.id.recommended_attraction_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommended_attraction_item, parent, false)
        return AttractionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        val attraction = getItem(position)
        holder.nameTextView.text = attraction.name
        holder.typeTextView.text = attraction.type
        
        // Set click listener to open the attraction details
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AttractionDetailActivity::class.java)
            intent.putExtra("ATTRACTION_ID", attraction.id)
            context.startActivity(intent)
        }
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