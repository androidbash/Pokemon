package com.harsh.pokemon.api

import com.harsh.pokemon.model.Pokemon

/**
 * Created by Harsh on 29/05/2019.
 */

data class ResponsePokemon(
        val results: List<Pokemon> = emptyList(),
        val next: String
)
