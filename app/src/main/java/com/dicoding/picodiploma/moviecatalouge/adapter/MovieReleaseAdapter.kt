package com.dicoding.picodiploma.moviecatalouge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import com.dicoding.picodiploma.moviecatalouge.R
import kotlinx.android.synthetic.main.item_movie_release.view.*

class MovieReleaseAdapter : RecyclerView.Adapter<MovieReleaseAdapter.MovieReleaseViewHolder>() {
    private val dataMovieRelease = ArrayList<MovieItems>()
    fun setDataMovieRelease(items: ArrayList<MovieItems>) {
        dataMovieRelease.clear()
        dataMovieRelease.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReleaseViewHolder {
        val viewMovieRelease = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_release, parent, false)
        return MovieReleaseViewHolder(viewMovieRelease)
    }

    override fun getItemCount(): Int = dataMovieRelease.size

    override fun onBindViewHolder(holder: MovieReleaseViewHolder, position: Int) {
        holder.bind(dataMovieRelease[position])
    }

    inner class MovieReleaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieItems: MovieItems) {
            with(itemView){
                txtTitleMovieRelease.text = movieItems.dataTitle.toString()
                txtOverviewMovieRelease.text = movieItems.dataOverview.toString()
            }
        }
    }
}