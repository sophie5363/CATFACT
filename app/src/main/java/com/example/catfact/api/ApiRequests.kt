package com.example.catfact.api


import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {

    @get:GET("pokedex.json")
    val listPokemon: Observable<Pokedex>

}