package com.example.istreamapp.ui.playlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.istreamapp.R
import com.example.istreamapp.data.AppDatabase
import com.example.istreamapp.ui.home.HomeFragment
import com.example.istreamapp.utils.SessionManager
import kotlinx.coroutines.launch

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.playlistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dao = AppDatabase.getDatabase(requireContext()).appDao()
        val username = SessionManager.getLoggedInUser(requireContext())

        lifecycleScope.launch {
            val list = dao.getPlaylistForUser(username!!)

            recyclerView.adapter = PlaylistAdapter(list) { clickedItem ->
                val bundle = Bundle().apply {
                    putString("video_url", clickedItem.videoUrl)
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment().apply { arguments = bundle })
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}