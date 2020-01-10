/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.moviecatalouge.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.activity.TVShowDetailActivity
import com.dicoding.picodiploma.moviecatalouge.adapter.TVShowFavoriteAdapter
import com.dicoding.picodiploma.moviecatalouge.helper.FavoriteHelper
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import kotlinx.android.synthetic.main.fragment_tvshow_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TVShowFavoriteFragment : Fragment() {
    private lateinit var tvshowFavoriteAdapter: TVShowFavoriteAdapter
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tvshow_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvshowFavoriteAdapter =
            TVShowFavoriteAdapter()
        tvshowFavoriteAdapter.notifyDataSetChanged()

        val activityFragment = activity as Context
        rvTVShowFavorite.layoutManager =  GridLayoutManager(activityFragment, 3)
        rvTVShowFavorite.adapter = tvshowFavoriteAdapter
        rvTVShowFavorite.setHasFixedSize(true)

        favoriteHelper = FavoriteHelper.getInstance(activityFragment.applicationContext)
        favoriteHelper.open()

        if (savedInstanceState == null) {
            loadTVShowFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<TVShowItems>(
                EXTRA_STATE
            )
            if (list != null) {
                tvshowFavoriteAdapter.listTVShowFavorite = list
            }
        }

        tvshowFavoriteAdapter.setOnItemClickCallback(object : TVShowFavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TVShowItems) {
                selectedTVShow(data)
            }
        })
    }

    private fun loadTVShowFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredTVShowFavorite = async(Dispatchers.IO) {
                favoriteHelper.getAllTVShowFavoriteArrayList()
            }

            val tvshowFavorite = deferredTVShowFavorite.await()

            if (tvshowFavorite.size > 0) {
                lblTVShowEmpty.visibility = View.INVISIBLE
                tvshowFavoriteAdapter.listTVShowFavorite = tvshowFavorite

            } else {
                lblTVShowEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun selectedTVShow(tvshow: TVShowItems) {
        val activityFragment = activity as Context
        val moveTVShowsDetail = Intent(activityFragment, TVShowDetailActivity::class.java)
        moveTVShowsDetail.putExtra(TVShowDetailActivity.EXTRA_TVSHOW, tvshow)
        moveTVShowsDetail.putExtra("FROM_ACTIVITY", "FAVORITE_ACTIVITY")
        startActivity(moveTVShowsDetail)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, tvshowFavoriteAdapter.listTVShowFavorite)
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }
}
