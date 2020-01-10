/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.moviecatalouge.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.adapter.SectionsFavoritePagerAdapter
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = getString(R.string.favorite)
        actionbar?.elevation = 0f

        val sectionsFavoritePagerAdapter =
            SectionsFavoritePagerAdapter(
                this,
                supportFragmentManager
            )
        viewFavoritePager.adapter = sectionsFavoritePagerAdapter
        tabsFavorite.setupWithViewPager(viewFavoritePager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val moveMainActivity = Intent(this, MainActivity::class.java)
            startActivity(moveMainActivity)
        }
        return super.onOptionsItemSelected(item)
    }
}
