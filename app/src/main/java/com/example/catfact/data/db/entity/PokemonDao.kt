package com.example.catfact.data.db.entity

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface PokemonDao {
    @Insert
    fun insertAll(vararg users: Pokemon)

    @Delete
    fun delete(user: Pokemon)

    @Query("SELECT * FROM Pokemon")
    fun getAll(): List<Pokemon>


}