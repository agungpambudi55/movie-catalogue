package com.dicoding.picodiploma.moviecatalouge.activity

import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.database.DatabaseContract
import com.dicoding.picodiploma.moviecatalouge.helper.FavoriteHelper
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = getString(R.string.movieDetail)

        showLoadingMovieDetail(true)

        val posterMovieDetail : ImageView = findViewById(R.id.imgPosterMovieDetail)
        val backdropMovieDetail : ImageView = findViewById(R.id.imgBackdropMovieDetail)
        val titleMovieDetail : TextView = findViewById(R.id.txtTitleMovieDetail)
        val voteMovieDetail : TextView = findViewById(R.id.txtVoteMovieDetail)
        val dateMovieDetail : TextView = findViewById(R.id.txtDateMovieDetail)
        val overviewMovieDetail : TextView = findViewById(R.id.txtOverviewMovieDetail)

        val movieDetail = intent.getParcelableExtra(EXTRA_MOVIE) as MovieItems

        val url = "https://image.tmdb.org/t/p/w500"

        Glide.with(this)
            .load(url+movieDetail.dataBackdropPath)
            .into(backdropMovieDetail)

        Glide.with(this)
            .load(url+movieDetail.dataPosterPath)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    showLoadingMovieDetail(false)
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .into(posterMovieDetail)

        titleMovieDetail.text = movieDetail.dataTitle
        voteMovieDetail.text = movieDetail.dataVoteAverage.toString()
        dateMovieDetail.text = movieDetail.dataReleaseDate
        overviewMovieDetail.text = movieDetail.dataOverview

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val valuesMovieFavorite = ContentValues()
        valuesMovieFavorite.put(DatabaseContract.MovieColumns.ID, movieDetail.dataId)
        valuesMovieFavorite.put(DatabaseContract.MovieColumns.TITLE, movieDetail.dataTitle)
        valuesMovieFavorite.put(DatabaseContract.MovieColumns.VOTE_AVERAGE, movieDetail.dataVoteAverage)
        valuesMovieFavorite.put(DatabaseContract.MovieColumns.RELEASE_DATE, movieDetail.dataReleaseDate)
        valuesMovieFavorite.put(DatabaseContract.MovieColumns.OVERVIEW, movieDetail.dataOverview)
        valuesMovieFavorite.put(DatabaseContract.MovieColumns.POSTER_PATH, movieDetail.dataPosterPath)
        valuesMovieFavorite.put(DatabaseContract.MovieColumns.BACKDROP_PATH, movieDetail.dataBackdropPath)

        val checkExist = favoriteHelper.isExistMovieFavorite(movieDetail.dataId.toString())

        if (checkExist == 0){
            btnMovieFavorite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
            btnMovieFavorite.setOnClickListener {
                val resultInsert = favoriteHelper.insertMovieFavorite(valuesMovieFavorite)

                if (resultInsert > 0) {
                    Toast.makeText(applicationContext, movieDetail.dataTitle + " " + getString(R.string.favSuccess), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, movieDetail.dataTitle + " " + getString(R.string.favFailed), Toast.LENGTH_SHORT).show()
                }

                moveActivity()
            }

        }
        else {
            btnMovieFavorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp  )
            btnMovieFavorite.setOnClickListener {
                val resultDelete = favoriteHelper.deleteMovieFavorite(movieDetail.dataId.toString())

                if (resultDelete > 0) {
                    Toast.makeText(applicationContext, movieDetail.dataTitle + " " + getString(R.string.unFavSuccess), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, movieDetail.dataTitle + " " + getString(R.string.unFavFailed), Toast.LENGTH_SHORT).show()
                }

                moveActivity()
            }
        }
    }

    private fun moveActivity() {
        val previousActivity = intent?.getStringExtra("FROM_ACTIVITY")
        if (previousActivity.equals("FAVORITE_ACTIVITY")){
            val moveFavoriteActivity = Intent(this, FavoriteActivity::class.java)
            startActivity(moveFavoriteActivity)
        }else if (previousActivity.equals("MAIN_ACTIVITY")){
            val moveMainActivity = Intent(this, MainActivity::class.java)
            startActivity(moveMainActivity)
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            moveActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoadingMovieDetail(state: Boolean) {
        if (state) {
            progressBarMovieDetail.visibility = View.VISIBLE
        } else {
            progressBarMovieDetail.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }
}

