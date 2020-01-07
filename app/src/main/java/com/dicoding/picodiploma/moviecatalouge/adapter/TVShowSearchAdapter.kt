package com.dicoding.picodiploma.moviecatalouge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import kotlinx.android.synthetic.main.item_tvshow_search.view.*

class TVShowSearchAdapter : RecyclerView.Adapter<TVShowSearchAdapter.TVShowSearchViewHolder>() {
    private val dataTVShowSearch = ArrayList<TVShowItems>()
    fun setDataTVShowSearch(items: ArrayList<TVShowItems>) {
        dataTVShowSearch.clear()
        dataTVShowSearch.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TVShowItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowSearchViewHolder {
        val viewTVShowSearch = LayoutInflater.from(parent.context).inflate(R.layout.item_tvshow_search, parent, false)
        return TVShowSearchViewHolder(viewTVShowSearch)
    }

    override fun getItemCount(): Int = dataTVShowSearch.size

    override fun onBindViewHolder(holder: TVShowSearchViewHolder, position: Int) {
        holder.bind(dataTVShowSearch[position])
    }

    inner class TVShowSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvshowSearchItems: TVShowItems) {
            with(itemView){
                val urlPoster = "https://image.tmdb.org/t/p/w185"

                Glide.with(itemView.context)
                    .load(urlPoster+tvshowSearchItems.dataPosterPath)
                    .into(imgTVShowSearchPoster)

                txtTVShowSearchVote.text = tvshowSearchItems.dataVoteAverage.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(tvshowSearchItems) }
            }
        }
    }
}