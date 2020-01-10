/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.moviecatalouge.remoteView

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.dicoding.picodiploma.moviecatalouge.R
import com.dicoding.picodiploma.moviecatalouge.helper.FavoriteHelper
import com.dicoding.picodiploma.moviecatalouge.widget.WidgetMovie
import java.util.ArrayList

internal class WidgetRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = ArrayList<Bitmap>()
    private lateinit var favoriteHelper: FavoriteHelper

    override fun onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(mContext)
        favoriteHelper.open()
    }

    override fun onDataSetChanged() {
        mWidgetItems = favoriteHelper.getAllMovieFavoriteWidget()
    }

    override fun onDestroy() {
        favoriteHelper.close()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_widget)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(WidgetMovie.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}