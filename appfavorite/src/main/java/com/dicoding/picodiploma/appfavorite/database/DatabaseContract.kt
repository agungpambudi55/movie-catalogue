package com.dicoding.picodiploma.appfavorite.database

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    companion object {
        const val AUTHORITY = "com.dicoding.picodiploma.moviecatalouge"
        const val SCHEME = "content"
    }

    internal class MovieColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "movie_favorite"
            const val ID = "id"
            const val TITLE = "title"
            const val VOTE_AVERAGE = "vote_average"
            const val RELEASE_DATE = "release_date"
            const val OVERVIEW = "overview"
            const val POSTER_PATH = "poster_path"
            const val BACKDROP_PATH = "backdrop_path"

            val CONTENT_URI_MOVIE: Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
                TABLE_NAME
            ).build()
        }
    }
}
