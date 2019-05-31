package com.harsh.pokemon.model

data class PokemonDetails(
        val base_experience: Int = 0,
        val height: Int = 0,
        val id: Int = 0,
        val is_default: Boolean = false,
        val location_area_encounters: String = "",
        val name: String = "",
        val order: Int = 0,
        val weight: Int = 0
)