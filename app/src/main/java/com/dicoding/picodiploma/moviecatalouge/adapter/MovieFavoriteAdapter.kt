/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.moviecatalouge.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import kotlinx.android.synthetic.main.item_movie_favorite.view.*
import java.util.ArrayList

class MovieFavoriteAdapter : RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteViewHolder>() {
    var listMovieFavorite = ArrayList<MovieItems>()
        set(listMovieFavorite) {
            this.listMovieFavorite.clear()
            this.listMovieFavorite.addAll(listMovieFavorite)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_favorite, parent, false)
        return MovieFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieFavoriteViewHolder, position: Int) {
        holder.bind(listMovieFavorite[position])
    }

    override fun getItemCount(): Int = this.listMovieFavorite.size

    inner class MovieFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieItems: MovieItems) {
            with(itemView){
                val urlPoster = "https://image.tmdb.org/t/p/w185"

                Glide.with(itemView.context)
                    .load(urlPoster+movieItems.dataPosterPath)
                    .into(imgMovieFavoritePoster)

                txtMovieFavoriteVote.text = movieItems.dataVoteAverage.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movieItems) }
            }
        }
    }
}