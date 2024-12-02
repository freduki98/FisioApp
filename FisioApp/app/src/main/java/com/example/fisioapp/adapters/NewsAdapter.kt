package com.example.fisioapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fisioapp.R
import com.example.fisioapp.models.NewsModel

class NewsAdapter(var lista: MutableList<NewsModel>): RecyclerView.Adapter<NewsViewHolder>() {

//class NewsAdapter(var lista: MutableList<NewsModel>,
//                     private val onItemClick: (NewsModel) -> Unit) : RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_api_layout, parent, false)
        return NewsViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val noticia = lista[position]
        holder.render(noticia)
        //holder.render(imagen, onItemClick)

    }

    override fun getItemCount(): Int {
        return lista.size
    }

}