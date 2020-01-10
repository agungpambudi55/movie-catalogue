/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.appfavorite.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.appfavorite.R
import com.dicoding.picodiploma.appfavorite.item.MovieItems
import kotlinx.android.synthetic.main.item_movie_favorite.view.*
import java.util.ArrayList

class MovieFavoriteAdapter : RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteViewHolder>() {
    var listMovieFavorite = ArrayList<MovieItems>()
        set(listMovieFavorite) {
            this.listMovieFavorite.clear()
            this.listMovieFavorite.addAll(listMovieFavorite)
            notifyDataSetChanged()
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
            }
        }
    }
}