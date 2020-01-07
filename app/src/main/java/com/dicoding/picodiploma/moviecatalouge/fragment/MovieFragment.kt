package com.dicoding.picodiploma.moviecatalouge.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.moviecatalouge.viewModel.MovieViewModel
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.activity.MovieDetailActivity
import com.dicoding.picodiploma.moviecatalouge.adapter.MovieAdapter
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter()
        movieAdapter.notifyDataSetChanged()

        val activityFragment = activity as Context
        rvMovie.layoutManager =  GridLayoutManager(activityFragment, 3)
        rvMovie.adapter = movieAdapter

        movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MovieViewModel::class.java)

        movieViewModel.setMovie()
        showLoadingMovie(true)

        movieViewModel.getMovie().observe(viewLifecycleOwner, Observer { movieItems ->
            if (movieItems != null) {
                movieAdapter.setDataMovie(movieItems)
                showLoadingMovie(false)
            }
        })

        movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: MovieItems) {
                selectedMovie(data)
            }
        })
    }

    private fun selectedMovie(movie: MovieItems) {
        val activityFragment = activity as Context
        val moveMoviesDetail = Intent(activityFragment, MovieDetailActivity::class.java)
        moveMoviesDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
        moveMoviesDetail.putExtra("FROM_ACTIVITY", "MAIN_ACTIVITY")
        startActivity(moveMoviesDetail)
    }

    private fun showLoadingMovie(state: Boolean) {
        if (state) {
            progressBarMovie.visibility = View.VISIBLE
        } else {
            progressBarMovie.visibility = View.GONE
        }
    }
}
