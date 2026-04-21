package com.example.sportsnewsfeedapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsnewsfeedapp.R
import com.example.sportsnewsfeedapp.model.NewsItem

class LatestNewsAdapter(
    private var newsList: List<NewsItem>,
    private val onItemClick: (NewsItem) -> Unit
) : RecyclerView.Adapter<LatestNewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgNews: ImageView = itemView.findViewById(R.id.imgNews)
        val txtNewsTitle: TextView = itemView.findViewById(R.id.txtNewsTitle)
        val txtNewsCategory: TextView = itemView.findViewById(R.id.txtNewsCategory)
        val txtNewsDescription: TextView = itemView.findViewById(R.id.txtNewsDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = newsList[position]
        holder.txtNewsTitle.text = item.title
        holder.txtNewsCategory.text = item.category
        holder.txtNewsDescription.text = item.description
        holder.imgNews.setImageResource(R.mipmap.ic_launcher)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = newsList.size

    fun updateList(newList: List<NewsItem>) {
        newsList = newList
        notifyDataSetChanged()
    }
}