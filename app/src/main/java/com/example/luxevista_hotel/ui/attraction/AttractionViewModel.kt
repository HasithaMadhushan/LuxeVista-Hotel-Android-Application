package com.example.luxevista_hotel.ui.attraction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.luxevista_hotel.data.Attraction
import com.example.luxevista_hotel.data.LuxeVistaRepository
import kotlinx.coroutines.launch

class AttractionViewModel(private val repository: LuxeVistaRepository) : ViewModel() {

    // Get all attractions
    val allAttractions: LiveData<List<Attraction>> = repository.allAttractions

    // Get attraction by ID
    fun getAttractionById(attractionId: Int): LiveData<Attraction> {
        return repository.getAttractionById(attractionId)
    }

    // Get attractions by type
    fun getAttractionsByType(type: String): LiveData<List<Attraction>> {
        return repository.getAttractionsByType(type)
    }

    // Get similar attractions (same type but excluding the current one)
    fun getSimilarAttractions(attractionId: Int): LiveData<List<Attraction>> {
        val currentAttraction = MutableLiveData<Attraction>()
        
        viewModelScope.launch {
            // First, get the current attraction to determine its type
            repository.getAttractionById(attractionId).observeForever { attraction ->
                if (attraction != null) {
                    currentAttraction.value = attraction
                }
            }
        }
        
        // Then, return attractions of the same type, excluding the current one
        return currentAttraction.map { current ->
            if (current != null) {
                repository.getAttractionsByType(current.type).value?.filter { it.id != current.id } ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    // Get recommended attractions based on user preferences
    fun getRecommendedAttractions(userId: Int): LiveData<List<Attraction>> {
        return repository.getRecommendedAttractionsForUser(userId)
    }
} 