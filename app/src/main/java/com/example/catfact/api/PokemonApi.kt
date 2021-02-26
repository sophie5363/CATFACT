package com.example.catfact.api

import com.example.catfact.api.PokemonApiJSON
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {
    @GET("pokedex.json")
    suspend fun getPokemons() : PokemonApiJSON
}