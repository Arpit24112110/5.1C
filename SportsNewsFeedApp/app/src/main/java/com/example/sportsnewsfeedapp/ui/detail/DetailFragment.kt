package com.example.sportsnewsfeedapp.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsnewsfeedapp.R
import com.example.sportsnewsfeedapp.adapter.LatestNewsAdapter
import com.example.sportsnewsfeedapp.data.DummyData
import com.example.sportsnewsfeedapp.model.NewsItem
import com.example.sportsnewsfeedapp.utils.BookmarkManager

class DetailFragment : Fragment(R.layout.fragment_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val img = view.findViewById<ImageView>(R.id.detailImage)
        val title = view.findViewById<TextView>(R.id.detailTitle)
        val description = view.findViewById<TextView>(R.id.detailDescription)
        val bookmarkBtn = view.findViewById<Button>(R.id.btnBookmark)
        val relatedRecycler = view.findViewById<RecyclerView>(R.id.relatedRecyclerView)

        val titleArg = arguments?.getString("title") ?: ""
        val descArg = arguments?.getString("description") ?: ""

        title.text = titleArg
        description.text = descArg
        img.setImageResource(R.mipmap.ic_launcher)

        bookmarkBtn.setOnClickListener {
            BookmarkManager.saveBookmark(requireContext(), titleArg)
            Toast.makeText(requireContext(), "Bookmarked!", Toast.LENGTH_SHORT).show()
        }

        val allNews = DummyData.getNewsList()

        val relatedList: List<NewsItem> = allNews.filter {
            it.title != titleArg
        }

        val adapter = LatestNewsAdapter(relatedList) { news ->
            val bundle = Bundle().apply {
                putString("title", news.title)
                putString("description", news.description)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailFragment().apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        }

        relatedRecycler.layoutManager = LinearLayoutManager(requireContext())
        relatedRecycler.adapter = adapter
    }
}