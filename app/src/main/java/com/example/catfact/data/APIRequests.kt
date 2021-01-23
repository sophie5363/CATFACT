package com.example.catfact.data

import com.example.catfact.data.network.response.PokemonApiJSON
import retrofit2.http.GET

interface APIRequests {
    @GET("pokedex.json")
    suspend fun getPokemons() : PokemonApiJSON
}