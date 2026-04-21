package com.example.sportsnewsfeedapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsnewsfeedapp.R
import com.example.sportsnewsfeedapp.adapter.FeaturedMatchesAdapter
import com.example.sportsnewsfeedapp.adapter.LatestNewsAdapter
import com.example.sportsnewsfeedapp.data.DummyData
import com.example.sportsnewsfeedapp.model.NewsItem
import com.example.sportsnewsfeedapp.ui.bookmarks.BookmarksFragment
import com.example.sportsnewsfeedapp.ui.detail.DetailFragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var latestNewsAdapter: LatestNewsAdapter
    private lateinit var allNews: List<NewsItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val featuredRecyclerView = view.findViewById<RecyclerView>(R.id.featuredRecyclerView)
        val newsRecyclerView = view.findViewById<RecyclerView>(R.id.newsRecyclerView)
        val searchBar = view.findViewById<EditText>(R.id.searchBar)
        val btnOpenBookmarks = view.findViewById<Button>(R.id.btnOpenBookmarks)

        val featuredMatches = DummyData.getFeaturedMatches()
        allNews = DummyData.getNewsList()

        val featuredAdapter = FeaturedMatchesAdapter(featuredMatches) { match ->
            val bundle = Bundle().apply {
                putString("title", match.title)
                putString("description", "Featured match details")
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailFragment().apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        }

        latestNewsAdapter = LatestNewsAdapter(allNews) { news ->
            val bundle = Bundle().apply {
                putString("title", news.title)
                putString("description", news.description)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailFragment().apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        }

        featuredRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        featuredRecyclerView.adapter = featuredAdapter

        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsRecyclerView.adapter = latestNewsAdapter

        btnOpenBookmarks.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookmarksFragment())
                .addToBackStack(null)
                .commit()
        }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNews(s.toString())
            }
        })
    }

    private fun filterNews(query: String) {
        val filteredList = if (query.isBlank()) {
            allNews
        } else {
            allNews.filter {
                it.category.contains(query, ignoreCase = true)
            }
        }
        latestNewsAdapter.updateList(filteredList)
    }
}