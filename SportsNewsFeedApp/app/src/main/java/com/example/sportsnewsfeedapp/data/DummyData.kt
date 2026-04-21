package com.example.sportsnewsfeedapp.data

import com.example.sportsnewsfeedapp.model.NewsItem
import com.example.sportsnewsfeedapp.model.MatchItem

object DummyData {

    fun getNewsList(): List<NewsItem> {
        return listOf(
            NewsItem(1, "Football Finals Tonight", "Big match between top teams", "https://via.placeholder.com/150", "Football"),
            NewsItem(2, "Cricket World Cup Update", "Latest updates from the match", "https://via.placeholder.com/150", "Cricket"),
            NewsItem(3, "Basketball League Highlights", "Top plays from last night", "https://via.placeholder.com/150", "Basketball"),
            NewsItem(4, "Tennis Championship", "Final match analysis", "https://via.placeholder.com/150", "Tennis")
        )
    }

    fun getFeaturedMatches(): List<MatchItem> {
        return listOf(
            MatchItem(1, "Team A vs Team B", "https://via.placeholder.com/150"),
            MatchItem(2, "Team C vs Team D", "https://via.placeholder.com/150"),
            MatchItem(3, "Team E vs Team F", "https://via.placeholder.com/150")
        )
    }
}