package com.dicoding.picodiploma.moviecatalouge.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TVShowViewModel : ViewModel() {
    companion object {
        private const val API_KEY = "0bcd9958c9660388b760bd82f34a2ac1"
    }

    val listTVShow = MutableLiveData<ArrayList<TVShowItems>>()

    internal fun setTVShow() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<TVShowItems>()
        var url = ""
        val language: String = Locale.getDefault().language

        if(language == "in"){
            url = "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=id"
        }else if(language == "en"){
            url = "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=en-US"
        }

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val tvshow = list.getJSONObject(i)

                        val tvshowItems =
                            TVShowItems(
                                tvshow.getInt("id"),
                                tvshow.getString("name"),
                                tvshow.getDouble("vote_average"),
                                tvshow.getString("first_air_date"),
                                tvshow.getString("overview"),
                                tvshow.getString("poster_path"),
                                tvshow.getString("backdrop_path")
                            )
                        listItems.add(tvshowItems)
                    }
                    listTVShow.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getTVShow(): LiveData<ArrayList<TVShowItems>> {
        return listTVShow
    }
}