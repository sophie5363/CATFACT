package com.example.catfact

import com.example.catfact.api.PokemonApiJSON
import retrofit2.http.GET

interface APIRequests {
    @GET("pokedex.json")
    suspend fun getPokemons() : PokemonApiJSON
}