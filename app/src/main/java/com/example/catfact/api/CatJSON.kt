package com.example.catfact.api

data class CatJSON(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val deleted: Boolean,
    val source: String,
    val status: Status,
    val text: String,
    val type: String,
    val updatedAt: String,
    val used: Boolean,
    val user: String
)