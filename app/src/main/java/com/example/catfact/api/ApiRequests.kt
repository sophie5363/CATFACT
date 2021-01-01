package com.example.catfact.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {

    @GET("pokemon/1")
    fun getPokemons(): Call<PokemonsJSON>

}