package com.example.sportsnewsfeedapp.utils

import android.content.Context

object BookmarkManager {

    private const val PREF_NAME = "bookmarks"
    private const val KEY_BOOKMARKS = "saved_items"

    fun saveBookmark(context: Context, title: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentSet = prefs.getStringSet(KEY_BOOKMARKS, mutableSetOf())?.toMutableSet()
            ?: mutableSetOf()

        currentSet.add(title)

        prefs.edit().putStringSet(KEY_BOOKMARKS, currentSet).apply()
    }

    fun getBookmarks(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
    }
}