package com.dicoding.picodiploma.moviecatalouge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import com.dicoding.picodiploma.moviecatalouge.R
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val dataMovie = ArrayList<MovieItems>()
    fun setDataMovie(items: ArrayList<MovieItems>) {
        dataMovie.clear()
        dataMovie.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val viewMovie = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(viewMovie)
    }

    override fun getItemCount(): Int = dataMovie.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(dataMovie[position])
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieItems: MovieItems) {
            with(itemView){
                val urlPoster = "https://image.tmdb.org/t/p/w185"

                Glide.with(itemView.context)
                    .load(urlPoster+movieItems.dataPosterPath)
                    .into(imgMoviePoster)

                txtMovieVote.text = movieItems.dataVoteAverage.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movieItems) }
            }
        }
    }
}