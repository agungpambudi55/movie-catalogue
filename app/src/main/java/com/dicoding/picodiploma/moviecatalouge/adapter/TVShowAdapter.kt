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
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import kotlinx.android.synthetic.main.item_tvshow.view.*

class TVShowAdapter : RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder>() {
    private val dataTVShow = ArrayList<TVShowItems>()
    fun setDataTVShow(items: ArrayList<TVShowItems>) {
        dataTVShow.clear()
        dataTVShow.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TVShowItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val viewTVShow = LayoutInflater.from(parent.context).inflate(R.layout.item_tvshow, parent, false)
        return TVShowViewHolder(viewTVShow)
    }

    override fun getItemCount(): Int = dataTVShow.size

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        holder.bind(dataTVShow[position])
    }

    inner class TVShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvshowItems: TVShowItems) {
            with(itemView){
                val urlPoster = "https://image.tmdb.org/t/p/w185"

                Glide.with(itemView.context)
                    .load(urlPoster+tvshowItems.dataPosterPath)
                    .into(imgTVShowPoster)

                txtTVShowVote.text = tvshowItems.dataVoteAverage.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(tvshowItems) }
            }
        }
    }
}