/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.moviecatalouge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import com.dicoding.picodiploma.moviecatalouge.R
import kotlinx.android.synthetic.main.item_movie_search.view.*

class MovieSearchAdapter : RecyclerView.Adapter<MovieSearchAdapter.MovieSearchViewHolder>() {
    private val dataMovieSearch = ArrayList<MovieItems>()
    fun setDataMovieSearch(items: ArrayList<MovieItems>) {
        dataMovieSearch.clear()
        dataMovieSearch.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchViewHolder {
        val viewMovieSearch = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_search, parent, false)
        return MovieSearchViewHolder(viewMovieSearch)
    }

    override fun getItemCount(): Int = dataMovieSearch.size

    override fun onBindViewHolder(holder: MovieSearchViewHolder, position: Int) {
        holder.bind(dataMovieSearch[position])
    }

    inner class MovieSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieSearchItems: MovieItems) {
            with(itemView){
                val urlPoster = "https://image.tmdb.org/t/p/w185"

                Glide.with(itemView.context)
                    .load(urlPoster+movieSearchItems.dataPosterPath)
                    .into(imgMovieSearchPoster)

                txtMovieSearchVote.text = movieSearchItems.dataVoteAverage.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movieSearchItems) }
            }
        }
    }
}