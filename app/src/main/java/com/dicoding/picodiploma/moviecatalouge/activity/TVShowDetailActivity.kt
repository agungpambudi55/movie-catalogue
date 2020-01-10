/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

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
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import kotlinx.android.synthetic.main.activity_tvshow_detail.*

class TVShowDetailActivity : AppCompatActivity() {
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        const val EXTRA_TVSHOW = "extra_tvshow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshow_detail)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = getString(R.string.tvshowDetail)

        showLoadingTVShowDetail(true)

        val posterTVShowDetail : ImageView = findViewById(R.id.imgPosterTVShowDetail)
        val backdropTVShowDetail : ImageView = findViewById(R.id.imgBackdropTVShowDetail)
        val titleTVShowDetail : TextView = findViewById(R.id.txtNameTVShowDetail)
        val voteTVShowDetail : TextView = findViewById(R.id.txtVoteTVShowDetail)
        val dateTVShowDetail : TextView = findViewById(R.id.txtDateTVShowDetail)
        val overviewTVShowDetail : TextView = findViewById(R.id.txtOverviewTVShowDetail)

        val tvshowDetail = intent.getParcelableExtra(EXTRA_TVSHOW) as TVShowItems

        val url = "https://image.tmdb.org/t/p/w500"

        Glide.with(this)
            .load(url+tvshowDetail.dataBackdropPath)
            .into(backdropTVShowDetail)

        Glide.with(this)
            .load(url+tvshowDetail.dataPosterPath)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    showLoadingTVShowDetail(false)
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .into(posterTVShowDetail)

        titleTVShowDetail.text = tvshowDetail.dataName
        voteTVShowDetail.text = tvshowDetail.dataVoteAverage.toString()
        dateTVShowDetail.text = tvshowDetail.dataFirstAirDate
        overviewTVShowDetail.text = tvshowDetail.dataOverview

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val valuesTVShowFavorite = ContentValues()
        valuesTVShowFavorite.put(DatabaseContract.TVShowColumns.ID, tvshowDetail.dataId)
        valuesTVShowFavorite.put(DatabaseContract.TVShowColumns.NAME, tvshowDetail.dataName)
        valuesTVShowFavorite.put(DatabaseContract.TVShowColumns.VOTE_AVERAGE, tvshowDetail.dataVoteAverage)
        valuesTVShowFavorite.put(DatabaseContract.TVShowColumns.FIRST_AIR_DATE, tvshowDetail.dataFirstAirDate)
        valuesTVShowFavorite.put(DatabaseContract.TVShowColumns.OVERVIEW, tvshowDetail.dataOverview)
        valuesTVShowFavorite.put(DatabaseContract.TVShowColumns.POSTER_PATH, tvshowDetail.dataPosterPath)
        valuesTVShowFavorite.put(DatabaseContract.TVShowColumns.BACKDROP_PATH, tvshowDetail.dataBackdropPath)

        val checkExist = favoriteHelper.isExistTVShowFavorite(tvshowDetail.dataId.toString())

        if (checkExist == 0){
            btnTVShowFavorite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
            btnTVShowFavorite.setOnClickListener {
                val resultInsert = favoriteHelper.insertTVShowFavorite(valuesTVShowFavorite)

                if (resultInsert > 0) {
                    Toast.makeText(applicationContext, tvshowDetail.dataName + " " + getString(R.string.favSuccess), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, tvshowDetail.dataName + " " + getString(R.string.favFailed), Toast.LENGTH_SHORT).show()
                }

                moveActivity()
            }

        }
        else {
            btnTVShowFavorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp  )
            btnTVShowFavorite.setOnClickListener {
                val resultDelete = favoriteHelper.deleteTVShowFavorite(tvshowDetail.dataId.toString())

                if (resultDelete > 0) {
                    Toast.makeText(applicationContext, tvshowDetail.dataName + " " + getString(R.string.unFavSuccess), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, tvshowDetail.dataName + " " + getString(R.string.unFavFailed), Toast.LENGTH_SHORT).show()
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

    private fun showLoadingTVShowDetail(state: Boolean) {
        if (state) {
            progressBarTVShowDetail.visibility = View.VISIBLE
        } else {
            progressBarTVShowDetail.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }
}

