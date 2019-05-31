package com.harsh.pokemon.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.harsh.pokemon.data.WebRepository
import com.harsh.pokemon.model.Pokemon
import com.harsh.pokemon.model.PokemonResult


/**
 * ViewModel for the [HomeActivity] screen.
 * The ViewModel works with the [WebRepository] to get the data.
 */
class PokemonRepositoriesViewModel(private val repository: WebRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val result: LiveData<PokemonResult> = Transformations.map(queryLiveData) {
        repository.responsePokemon()
    }

    val repos: LiveData<List<Pokemon>> = Transformations.switchMap(
            result
    ) { it.data }

    /**
     * Search a repository based on a query string.
     */
    fun getPokemon() {
        queryLiveData.postValue("start")
    }

    fun listScrolled() {
        repository.requestMore()
    }

}