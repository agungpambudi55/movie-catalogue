/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.appfavorite.activity

import android.annotation.SuppressLint
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.appfavorite.R
import com.dicoding.picodiploma.appfavorite.adapter.MovieFavoriteAdapter
import com.dicoding.picodiploma.appfavorite.database.DatabaseContract.MovieColumns.Companion.CONTENT_URI_MOVIE
import com.dicoding.picodiploma.appfavorite.helper.MappingHelper
import com.dicoding.picodiploma.appfavorite.item.MovieItems
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var movieFavoriteAdapter: MovieFavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieFavoriteAdapter =
            MovieFavoriteAdapter()
        movieFavoriteAdapter.notifyDataSetChanged()

        rvMovieFavorite.layoutManager =  GridLayoutManager(this@MainActivity, 3)
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

        contentResolver.registerContentObserver(CONTENT_URI_MOVIE, true, myObserver)

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
    }

    @SuppressLint("Recycle")
    private fun loadMovieFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovieFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI_MOVIE, null, null, null, null)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, movieFavoriteAdapter.listMovieFavorite)
    }
}
