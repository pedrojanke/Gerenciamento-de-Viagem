package com.example.gerenciamentodeviagem.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciamentodeviagem.data.models.Travel
import com.example.gerenciamentodeviagem.data.repository.TravelRepository
import kotlinx.coroutines.launch

class TravelViewModel(private val travelRepository: TravelRepository) : ViewModel() {

    var travels = mutableStateListOf<Travel>()
        private set

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    init {
    }

    fun loadTravels(userId: Int) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                travels.clear()
                travels.addAll(travelRepository.getTravelsByUserId(userId))
            } catch (e: Exception) {
                errorMessage.value = "Failed to load travels: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun addTravel(travel: Travel) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                travelRepository.addTravel(travel)
                travels.add(travel)
            } catch (e: Exception) {
                errorMessage.value = "Failed to add travel: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun removeTravel(travel: Travel) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                travelRepository.removeTravel(travel)
                travels.remove(travel)
            } catch (e: Exception) {
                errorMessage.value = "Failed to remove travel: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateTravel(updatedTravel: Travel) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                travelRepository.updateTravel(updatedTravel)
                val index = travels.indexOfFirst { it.id == updatedTravel.id }
                if (index != -1) {
                    travels[index] = updatedTravel
                }
            } catch (e: Exception) {
                errorMessage.value = "Failed to update travel: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getTravelById(id: String): Travel? {
        val travelId = id.toIntOrNull()
        return if (travelId != null) {
            travels.find { it.id == travelId }
        } else {
            null
        }
    }
}
