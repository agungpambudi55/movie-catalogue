package com.dicoding.picodiploma.moviecatalouge.fragment

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.activity.MovieDetailActivity
import com.dicoding.picodiploma.moviecatalouge.adapter.MovieFavoriteAdapter
import com.dicoding.picodiploma.moviecatalouge.database.DatabaseContract.MovieColumns.Companion.CONTENT_URI_MOVIE
import com.dicoding.picodiploma.moviecatalouge.helper.MappingHelper
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import kotlinx.android.synthetic.main.fragment_movie_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MovieFavoriteFragment : Fragment() {
    private lateinit var movieFavoriteAdapter: MovieFavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieFavoriteAdapter =
            MovieFavoriteAdapter()
        movieFavoriteAdapter.notifyDataSetChanged()

        val activityFragment = activity as Context
        rvMovieFavorite.layoutManager =  GridLayoutManager(activityFragment, 3)
        rvMovieFavorite.adapter = movieFavoriteAdapter
        rvMovieFavorite.setHasFixedSize(true)

        val handlerThread = HandlerThread("DataObserverMovie")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadMovieFavoriteAsync()
            }
        }

        context!!.contentResolver.registerContentObserver(CONTENT_URI_MOVIE, true, myObserver)

        if (savedInstanceState == null) {
            loadMovieFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<MovieItems>(
                EXTRA_STATE
            )
            if (list != null) {
                movieFavoriteAdapter.listMovieFavorite = list
            }
        }

        movieFavoriteAdapter.setOnItemClickCallback(object : MovieFavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: MovieItems) {
                selectedMovie(data)
            }
        })
    }

    private fun loadMovieFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovieFavorite = async(Dispatchers.IO) {
                val cursor = context!!.contentResolver.query(CONTENT_URI_MOVIE, null, null, null, null)
                MappingHelper.mapCursorToArrayListMovie(cursor!!)
            }

            val movieFavorite = deferredMovieFavorite.await()

            if (movieFavorite.size > 0) {
                lblMovieEmpty.visibility = View.INVISIBLE
                movieFavoriteAdapter.listMovieFavorite = movieFavorite
            } else {
                lblMovieEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun selectedMovie(movie: MovieItems) {
        val activityFragment = activity as Context
        val moveMoviesDetail = Intent(activityFragment, MovieDetailActivity::class.java)
        moveMoviesDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
        moveMoviesDetail.putExtra("FROM_ACTIVITY", "FAVORITE_ACTIVITY")
        startActivity(moveMoviesDetail)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, movieFavoriteAdapter.listMovieFavorite)
    }
}
