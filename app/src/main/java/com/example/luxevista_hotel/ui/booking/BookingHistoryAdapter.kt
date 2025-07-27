package com.example.luxevista_hotel.ui.booking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.luxevista_hotel.R
import com.example.luxevista_hotel.data.Booking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookingHistoryAdapter(
    private val context: Context,
    private val onCancelBooking: ((Int) -> Unit)? = null
) : ListAdapter<Booking, BookingHistoryAdapter.BookingViewHolder>(BookingDiffCallback()) {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookingTypeTextView: TextView = view.findViewById(R.id.booking_type_text)
        val bookingDateTextView: TextView = view.findViewById(R.id.booking_date_text)
        val bookingStatusBadge: TextView = view.findViewById(R.id.booking_status_badge)
        val statusAccentBar: View = view.findViewById(R.id.status_accent_bar)
        val bookingPriceTextView: TextView = view.findViewById(R.id.booking_price_text)
        val cancelButton: Button = view.findViewById(R.id.cancel_booking_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_history_item, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = getItem(position)
        
        // Determine if it's a room or service booking
        val bookingType = if (booking.roomId != null) "Room Booking" else "Service Booking"
        holder.bookingTypeTextView.text = bookingType
        
        // Format dates
        val dateText = if (booking.checkInDate == booking.checkOutDate) {
            // Service booking (single day)
            "Date: ${dateFormat.format(Date(booking.checkInDate))}"
        } else {
            // Room booking (range of days)
            "Check-in: ${dateFormat.format(Date(booking.checkInDate))}\n" +
            "Check-out: ${dateFormat.format(Date(booking.checkOutDate))}"
        }
        holder.bookingDateTextView.text = dateText
        
        // Set status badge
        holder.bookingStatusBadge.text = booking.bookingStatus
        when (booking.bookingStatus) {
            "Confirmed" -> {
                holder.bookingStatusBadge.setBackgroundResource(R.color.success)
                holder.statusAccentBar.setBackgroundResource(R.color.success)
            }
            "Cancelled" -> {
                holder.bookingStatusBadge.setBackgroundResource(R.color.error)
                holder.statusAccentBar.setBackgroundResource(R.color.error)
            }
            "Completed" -> {
                holder.bookingStatusBadge.setBackgroundResource(R.color.secondary)
                holder.statusAccentBar.setBackgroundResource(R.color.secondary)
            }
            else -> {
                holder.bookingStatusBadge.setBackgroundResource(R.color.info)
                holder.statusAccentBar.setBackgroundResource(R.color.info)
            }
        }
        
        // Set price
        holder.bookingPriceTextView.text = "Total: $${String.format("%.2f", booking.totalPrice)}"
        
        // Show cancel button only for upcoming bookings with "Confirmed" status
        if (booking.bookingStatus == "Confirmed" && booking.checkInDate > System.currentTimeMillis()) {
            holder.cancelButton.visibility = View.VISIBLE
            holder.cancelButton.setOnClickListener {
                onCancelBooking?.invoke(booking.id)
            }
        } else {
            holder.cancelButton.visibility = View.GONE
        }
    }
}

class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
    override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
        return oldItem == newItem
    }
} 