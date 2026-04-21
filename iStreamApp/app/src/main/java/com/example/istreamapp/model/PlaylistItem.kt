package com.example.istreamapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_items")
data class PlaylistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val videoUrl: String
)