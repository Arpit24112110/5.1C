package com.example.istreamapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.istreamapp.model.PlaylistItem
import com.example.istreamapp.model.User

@Dao
interface AppDao {

    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    @Insert
    suspend fun insertPlaylistItem(item: PlaylistItem): Long

    @Query("SELECT * FROM playlist_items WHERE username = :username")
    suspend fun getPlaylistForUser(username: String): List<PlaylistItem>
}