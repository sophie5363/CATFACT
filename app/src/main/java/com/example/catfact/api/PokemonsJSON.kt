package com.example.catfact.api

data class PokemonsJSON(

    val base_experience: Int,
    val height: Int,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val name: String,
    val order: Int,
    val weight: Int
)