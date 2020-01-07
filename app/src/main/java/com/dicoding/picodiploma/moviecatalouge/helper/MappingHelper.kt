package com.dicoding.picodiploma.moviecatalouge.helper

import android.database.Cursor
import com.dicoding.picodiploma.moviecatalouge.database.DatabaseContract
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import java.util.*

object MappingHelper {
    fun mapCursorToArrayListMovie(movieCursor: Cursor): ArrayList<MovieItems> {
        val movieList = ArrayList<MovieItems>()

        while (movieCursor.moveToNext()) {
            val dataId = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.ID))
            val dataTitle = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE))
            val dataVoteAverage = movieCursor.getDouble(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTE_AVERAGE))
            val dataReleaseDate = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE))
            val dataOverview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW))
            val dataPosterPath = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH))
            val dataBackdropPath = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.BACKDROP_PATH))
            movieList.add(MovieItems(dataId, dataTitle, dataVoteAverage, dataReleaseDate, dataOverview, dataPosterPath, dataBackdropPath))
        }

        return movieList
    }
}
