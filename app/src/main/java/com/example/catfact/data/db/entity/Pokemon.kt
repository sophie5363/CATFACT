package com.example.catfact.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon(

    val height: String,
    @PrimaryKey val id: Int,
    val img: String,
    val name: String,
    val num: String,
    val type: List<String>,
    val weight: String
)