package com.example.sportsnewsfeedapp.model

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: String
)