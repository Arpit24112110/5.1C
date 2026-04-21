package com.example.sportsnewsfeedapp.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsnewsfeedapp.R
import com.example.sportsnewsfeedapp.adapter.LatestNewsAdapter
import com.example.sportsnewsfeedapp.data.DummyData
import com.example.sportsnewsfeedapp.model.NewsItem
import com.example.sportsnewsfeedapp.ui.detail.DetailFragment
import com.example.sportsnewsfeedapp.utils.BookmarkManager

class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.bookmarksRecyclerView)

        val bookmarkedTitles = BookmarkManager.getBookmarks(requireContext())
        val allNews = DummyData.getNewsList()

        val bookmarkedNews: List<NewsItem> = allNews.filter {
            bookmarkedTitles.contains(it.title)
        }

        val adapter = LatestNewsAdapter(bookmarkedNews) { news ->
            val bundle = Bundle().apply {
                putString("title", news.title)
                putString("description", news.description)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailFragment().apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}