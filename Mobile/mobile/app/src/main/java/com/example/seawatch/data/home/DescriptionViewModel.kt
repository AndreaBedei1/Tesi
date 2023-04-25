package com.example.seawatch.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DescriptionViewModel(private val repository: DescriptionRepository): ViewModel() {
    val all=repository.all
    var d=""

    fun insert(description: Description)=viewModelScope.launch {
        repository.insert(description)
    }

    fun populate()=viewModelScope.launch {
        repository.insert(Description("2", "Altro", "Capodoglio", "Descrizione del capodoglio."))
        repository.insert(Description("1", "Altro", "Pesce Luna", "Descrizione del pesce luna."))
    }

    fun deleteAll()=viewModelScope.launch {
        repository.deleteAll()
    }

    fun getDescription(animale:String, specie:String)=viewModelScope.launch {
        d=repository.getDescription(animale, specie)
    }



}