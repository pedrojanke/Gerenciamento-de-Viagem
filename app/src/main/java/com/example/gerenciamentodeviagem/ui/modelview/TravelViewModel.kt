package com.example.gerenciamentodeviagem.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciamentodeviagem.repository.TravelRepository
import com.example.travelapp.data.database.TravelDatabase
import com.example.travelapp.data.model.Travel
import kotlinx.coroutines.launch

class TravelViewModel(private val repository: TravelRepository) : ViewModel() {

    // LiveData para armazenar todas as viagens
    private val _allTravels = MutableLiveData<List<Travel>>()
    val allTravels: LiveData<List<Travel>> get() = _allTravels

    // Função para obter todas as viagens
    fun getAllTravels() {
        viewModelScope.launch {
            _allTravels.postValue(repository.getAllTravels())
        }
    }

    // Função para obter uma viagem por ID
    fun getTravelById(travelId: Int): Travel? {
        var travel: Travel? = null
        viewModelScope.launch {
            travel = repository.getTravelById(travelId)
        }
        return travel
    }

    // Função para adicionar uma viagem
    fun addTravel(travel: Travel) {
        viewModelScope.launch {
            repository.addTravel(travel)
            getAllTravels() // Atualiza a lista de viagens após adicionar
        }
    }

    // Função para atualizar uma viagem
    fun updateTravel(travel: Travel) {
        viewModelScope.launch {
            repository.updateTravel(travel)
            getAllTravels() // Atualiza a lista de viagens após atualizar
        }
    }

    // Função para excluir uma viagem
    fun deleteTravel(travel: Travel) {
        viewModelScope.launch {
            repository.deleteTravel(travel)
            getAllTravels() // Atualiza a lista de viagens após deletar
        }
    }
}
