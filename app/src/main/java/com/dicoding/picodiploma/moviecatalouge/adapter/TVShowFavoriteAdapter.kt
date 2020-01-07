package com.dicoding.picodiploma.moviecatalouge.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import kotlinx.android.synthetic.main.item_tvshow_favorite.view.*
import java.util.ArrayList

class TVShowFavoriteAdapter : RecyclerView.Adapter<TVShowFavoriteAdapter.TVShowFavoriteViewHolder>() {
    var listTVShowFavorite = ArrayList<TVShowItems>()
        set(listTVShowFavorite) {
            this.listTVShowFavorite.clear()
            this.listTVShowFavorite.addAll(listTVShowFavorite)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TVShowItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tvshow_favorite, parent, false)
        return TVShowFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: TVShowFavoriteViewHolder, position: Int) {
        holder.bind(listTVShowFavorite[position])
    }

    override fun getItemCount(): Int = this.listTVShowFavorite.size

    inner class TVShowFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvshowItems: TVShowItems) {
            with(itemView){
                val urlPoster = "https://image.tmdb.org/t/p/w185"

                Glide.with(itemView.context)
                    .load(urlPoster+tvshowItems.dataPosterPath)
                    .into(imgTVShowFavoritePoster)

                txtTVShowFavoriteVote.text = tvshowItems.dataVoteAverage.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(tvshowItems) }
            }
        }
    }
}