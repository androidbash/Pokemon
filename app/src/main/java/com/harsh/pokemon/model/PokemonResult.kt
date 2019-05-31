package com.harsh.pokemon.model

import androidx.lifecycle.LiveData


data class PokemonResult(
        val data: LiveData<List<Pokemon>>
)