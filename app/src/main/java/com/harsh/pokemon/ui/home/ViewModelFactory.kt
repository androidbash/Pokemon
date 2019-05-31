package com.harsh.pokemon.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harsh.pokemon.data.WebRepository


/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: WebRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonRepositoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}