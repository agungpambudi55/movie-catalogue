package com.dicoding.picodiploma.moviecatalouge.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.adapter.MovieSearchAdapter
import com.dicoding.picodiploma.moviecatalouge.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.moviecatalouge.adapter.TVShowSearchAdapter
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import com.dicoding.picodiploma.moviecatalouge.viewModel.MovieSearchViewModel
import com.dicoding.picodiploma.moviecatalouge.viewModel.TVShowSearchViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var movieSearchAdapter: MovieSearchAdapter
    private lateinit var tvshowSearchAdapter: TVShowSearchAdapter
    private lateinit var movieSearchViewModel: MovieSearchViewModel
    private lateinit var tvshowSearchViewModel: TVShowSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0f

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
        val menuItemSearch = menu.findItem(R.id.actionSearch)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.searchHint)
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Toast.makeText(this@MainActivity, newText, Toast.LENGTH_SHORT).show()

                movieSearchAdapter = MovieSearchAdapter()
                movieSearchAdapter.notifyDataSetChanged()

                movieSearchViewModel = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory()).get(
                    MovieSearchViewModel::class.java)

                movieSearchViewModel.setMovieSearch(newText)

                movieSearchViewModel.getMovieSearch().observe(this@MainActivity, Observer { movieSearchItems ->
                    if (movieSearchItems != null) {
                        movieSearchAdapter.setDataMovieSearch(movieSearchItems)
                    }
                })

                movieSearchAdapter.setOnItemClickCallback(object : MovieSearchAdapter.OnItemClickCallback{
                    override fun onItemClicked(data: MovieItems) {
                        selectedMovieSearch(data)
                    }
                })
                rvMovieSearch.layoutManager =  GridLayoutManager(this@MainActivity, 25)
                rvMovieSearch.adapter = movieSearchAdapter

                tvshowSearchAdapter = TVShowSearchAdapter()
                tvshowSearchAdapter.notifyDataSetChanged()

                tvshowSearchViewModel = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory()).get(
                    TVShowSearchViewModel::class.java)

                tvshowSearchViewModel.setTVShowSearch(newText)

                tvshowSearchViewModel.getTVShowSearch().observe(this@MainActivity, Observer { tvshowSearchItems ->
                    if (tvshowSearchItems != null) {
                        tvshowSearchAdapter.setDataTVShowSearch(tvshowSearchItems)
                    }
                })

                tvshowSearchAdapter.setOnItemClickCallback(object : TVShowSearchAdapter.OnItemClickCallback{
                    override fun onItemClicked(data: TVShowItems) {
                        selectedTVShowSearch(data)
                    }
                })

                rvTVShowSearch.layoutManager =  GridLayoutManager(this@MainActivity, 25)
                rvTVShowSearch.adapter = tvshowSearchAdapter

                return false
            }
        })

        menuItemSearch.setOnActionExpandListener(object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                tabs.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                rvMovieSearch.visibility = View.GONE
                rvTVShowSearch.visibility = View.GONE
                lblMovieSearch.visibility = View.GONE
                lblTVShowSearch.visibility = View.GONE
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionSetting) {
            val moveSetting = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(moveSetting)
        }else if (item.itemId == R.id.actionReminder) {
            val moveReminder = Intent(this@MainActivity, ReminderActivity::class.java)
            startActivity(moveReminder)
        }else if (item.itemId == R.id.actionFavorite) {
            val moveFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(moveFavorite)
        }else if (item.itemId == R.id.actionSearch) {
            tabs.visibility = View.GONE
            viewPager.visibility = View.GONE
            rvMovieSearch.visibility = View.VISIBLE
            rvTVShowSearch.visibility = View.VISIBLE
            lblMovieSearch.visibility = View.VISIBLE
            lblTVShowSearch.visibility = View.VISIBLE

        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectedMovieSearch(movie: MovieItems) {
        val moveMoviesDetail = Intent(this@MainActivity, MovieDetailActivity::class.java)
        moveMoviesDetail.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie)
        moveMoviesDetail.putExtra("FROM_ACTIVITY", "MAIN_ACTIVITY")
        startActivity(moveMoviesDetail)
    }

    private fun selectedTVShowSearch(tvshow: TVShowItems) {
        val moveTVShowsDetail = Intent(this@MainActivity, TVShowDetailActivity::class.java)
        moveTVShowsDetail.putExtra(TVShowDetailActivity.EXTRA_TVSHOW, tvshow)
        moveTVShowsDetail.putExtra("FROM_ACTIVITY", "MAIN_ACTIVITY")
        startActivity(moveTVShowsDetail)

    }
}
