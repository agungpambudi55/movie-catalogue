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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.viewModel.TVShowViewModel
import com.dicoding.picodiploma.moviecatalouge.activity.TVShowDetailActivity
import com.dicoding.picodiploma.moviecatalouge.adapter.TVShowAdapter
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import kotlinx.android.synthetic.main.fragment_tvshow.*

class TVShowFragment : Fragment() {
    private lateinit var tvshowAdapter: TVShowAdapter
    private lateinit var tvshowViewModel: TVShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvshowAdapter =
            TVShowAdapter()
        tvshowAdapter.notifyDataSetChanged()

        val activityFragment = activity as Context
        rvTVShow.layoutManager =  GridLayoutManager(activityFragment, 3)
        rvTVShow.adapter = tvshowAdapter

        tvshowViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            TVShowViewModel::class.java)

        tvshowViewModel.setTVShow()
        showLoadingTVShow(true)

        tvshowViewModel.getTVShow().observe(viewLifecycleOwner, Observer { tvshowItems ->
            if (tvshowItems != null) {
                tvshowAdapter.setDataTVShow(tvshowItems)
                showLoadingTVShow(false)
            }
        })

        tvshowAdapter.setOnItemClickCallback(object : TVShowAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TVShowItems) {
                selectedTVShow(data)
            }
        })
    }

    private fun selectedTVShow(tvshow: TVShowItems) {
        val activityFragment = activity as Context
        val moveTVShowsDetail = Intent(activityFragment, TVShowDetailActivity::class.java)
        moveTVShowsDetail.putExtra(TVShowDetailActivity.EXTRA_TVSHOW, tvshow)
        moveTVShowsDetail.putExtra("FROM_ACTIVITY", "MAIN_ACTIVITY")
        startActivity(moveTVShowsDetail)

    }

    private fun showLoadingTVShow(state: Boolean) {
        if (state) {
            progressBarTVShow.visibility = View.VISIBLE
        } else {
            progressBarTVShow.visibility = View.GONE
        }
    }
}
