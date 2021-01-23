package com.example.catfact.data.network.response

import com.example.catfact.data.db.entity.Pokemon

data class PokemonApiJSON(
    val pokemon: List<Pokemon>
)