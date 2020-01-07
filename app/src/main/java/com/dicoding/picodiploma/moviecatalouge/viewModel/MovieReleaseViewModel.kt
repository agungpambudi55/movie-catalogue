package com.dicoding.picodiploma.moviecatalouge.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MovieReleaseViewModel : ViewModel() {
    companion object {
        private const val API_KEY = "0bcd9958c9660388b760bd82f34a2ac1"
    }

    val listMovieRelease = MutableLiveData<ArrayList<MovieItems>>()

    @SuppressLint("SimpleDateFormat")
    internal fun setMovieRelease() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<MovieItems>()

        val sdf = SimpleDateFormat("YYYY-MM-dd")
        val now = sdf.format(Date())
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&primary_release_date.gte=$now&primary_release_date.lte=$now"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)

                        val movieItems =
                            MovieItems(
                                movie.getInt("id"),
                                movie.getString("title"),
                                movie.getDouble("vote_average"),
                                movie.getString("release_date"),
                                movie.getString("overview"),
                                movie.getString("poster_path"),
                                movie.getString("backdrop_path")
                            )
                        listItems.add(movieItems)
                    }
                    listMovieRelease.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getMovieRelease(): LiveData<ArrayList<MovieItems>> {
        return listMovieRelease
    }
}