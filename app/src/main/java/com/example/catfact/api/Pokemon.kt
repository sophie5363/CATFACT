package com.example.catfact.api

data class Pokemon(
    val candy: String,
    val candy_count: Int,
    val egg: String,
    val height: String,
    val id: Int,
    val img: String,
    val multipliers: List<Double>,
    val name: String,
    val num: String,
    val spawn_chance: Double,
    val spawn_time: String,
    val type: List<String>,
    val weaknesses: List<String>,
    val weight: String
)