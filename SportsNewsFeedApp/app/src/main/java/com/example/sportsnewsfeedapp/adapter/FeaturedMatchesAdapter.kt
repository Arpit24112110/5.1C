package com.example.sportsnewsfeedapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsnewsfeedapp.R
import com.example.sportsnewsfeedapp.model.MatchItem

class FeaturedMatchesAdapter(
    private val matchList: List<MatchItem>,
    private val onItemClick: (MatchItem) -> Unit
) : RecyclerView.Adapter<FeaturedMatchesAdapter.MatchViewHolder>() {

    class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgMatch: ImageView = itemView.findViewById(R.id.imgMatch)
        val txtMatchTitle: TextView = itemView.findViewById(R.id.txtMatchTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val item = matchList[position]
        holder.txtMatchTitle.text = item.title
        holder.imgMatch.setImageResource(R.mipmap.ic_launcher)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = matchList.size
}