package com.dicoding.picodiploma.moviecatalouge.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MovieSearchViewModel : ViewModel() {
    companion object {
        private const val API_KEY = "0bcd9958c9660388b760bd82f34a2ac1"
    }

    val listMovieSearch = MutableLiveData<ArrayList<MovieItems>>()

    internal fun setMovieSearch(stringSearch: String) {
        val client = AsyncHttpClient()
        val listItems = ArrayList<MovieItems>()
        var url = ""
        val language: String = Locale.getDefault().language

        if(language == "in"){
            url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=id&query=$stringSearch"
        }else if(language == "en"){
            url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&query=$stringSearch"
        }

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movieSearch = list.getJSONObject(i)

                        val movieSearchItems =
                            MovieItems(
                                movieSearch.getInt("id"),
                                movieSearch.getString("title"),
                                movieSearch.getDouble("vote_average"),
                                movieSearch.getString("release_date"),
                                movieSearch.getString("overview"),
                                movieSearch.getString("poster_path"),
                                movieSearch.getString("backdrop_path")
                            )
                        listItems.add(movieSearchItems)
                    }
                    listMovieSearch.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getMovieSearch(): LiveData<ArrayList<MovieItems>> {
        return listMovieSearch
    }
}