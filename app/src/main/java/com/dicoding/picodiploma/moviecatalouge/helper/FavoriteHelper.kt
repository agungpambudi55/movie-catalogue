/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.moviecatalouge.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*
import com.dicoding.picodiploma.moviecatalouge.database.DatabaseContract
import com.dicoding.picodiploma.moviecatalouge.item.MovieItems
import com.dicoding.picodiploma.moviecatalouge.item.TVShowItems
import java.net.URL

class FavoriteHelper(context: Context) {

    companion object {
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var sqLiteDatabase: SQLiteDatabase

        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            FavoriteHelper(
                                context
                            )
                    }
                }
            }
            return INSTANCE as FavoriteHelper
        }
    }

    init {
        databaseHelper =
            DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        sqLiteDatabase = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (sqLiteDatabase.isOpen)
            sqLiteDatabase.close()
    }

    fun queryAllMovieFavoriteCursor(): Cursor {
        return sqLiteDatabase.query(
            DatabaseContract.MovieColumns.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseContract.MovieColumns.TITLE} ASC",
            null)
    }

    fun queryAllTVShowFavoriteCursor(): Cursor {
        return sqLiteDatabase.query(
            DatabaseContract.TVShowColumns.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseContract.TVShowColumns.NAME} ASC",
            null)
    }

    @SuppressLint("Recycle")
    fun isExistMovieFavorite(primaryKey: String): Int{
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM ${DatabaseContract.MovieColumns.TABLE_NAME} WHERE ID = '" + primaryKey + "'", null)
        return if (cursor.moveToFirst()) {
            1
        } else {
            0
        }
    }

    @SuppressLint("Recycle")
    fun isExistTVShowFavorite(primaryKey: String): Int{
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM ${DatabaseContract.TVShowColumns.TABLE_NAME} WHERE ID = '" + primaryKey + "'", null)
        return if (cursor.moveToFirst()) {
            1
        } else {
            0
        }
    }

    fun insertMovieFavorite(values: ContentValues?): Long {
        return sqLiteDatabase.insert(
            DatabaseContract.MovieColumns.TABLE_NAME, null, values)
    }

    fun insertTVShowFavorite(values: ContentValues?): Long {
        return sqLiteDatabase.insert(
            DatabaseContract.TVShowColumns.TABLE_NAME, null, values)
    }

    fun deleteMovieFavorite(id: String): Int {
        return sqLiteDatabase.delete(
            DatabaseContract.MovieColumns.TABLE_NAME, "${DatabaseContract.MovieColumns.ID} = '$id'", null)
    }

    fun deleteTVShowFavorite(id: String): Int {
        return sqLiteDatabase.delete(
            DatabaseContract.TVShowColumns.TABLE_NAME, "${DatabaseContract.TVShowColumns.ID} = '$id'", null)
    }

    fun getAllMovieFavoriteArrayList(): ArrayList<MovieItems> {
        val arrayList = ArrayList<MovieItems>()
        val cursor = sqLiteDatabase.query(
            DatabaseContract.MovieColumns.TABLE_NAME, null, null, null, null, null,
                "${DatabaseContract.MovieColumns.TITLE} ASC", null)

        cursor.moveToFirst()
        var movie: MovieItems
        if (cursor.count > 0) {
            do {
                movie = MovieItems(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.VOTE_AVERAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.RELEASE_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.BACKDROP_PATH))
                )

                arrayList.add(movie)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun getAllTVShowFavoriteArrayList(): ArrayList<TVShowItems> {
        val arrayList = ArrayList<TVShowItems>()
        val cursor = sqLiteDatabase.query(
            DatabaseContract.TVShowColumns.TABLE_NAME, null, null, null, null, null,
            "${DatabaseContract.TVShowColumns.NAME} ASC", null)

        cursor.moveToFirst()
        var tvshow: TVShowItems
        if (cursor.count > 0) {
            do {
                tvshow =
                    TVShowItems(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.VOTE_AVERAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.FIRST_AIR_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.OVERVIEW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.BACKDROP_PATH))
                    )

                arrayList.add(tvshow)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun getAllMovieFavoriteWidget(): ArrayList<Bitmap> {
        val arrayList = ArrayList<Bitmap>()
        val urlPoster = "https://image.tmdb.org/t/p/w185"
        val cursor = sqLiteDatabase.query(
            DatabaseContract.MovieColumns.TABLE_NAME, null, null, null, null, null,
            "${DatabaseContract.MovieColumns.TITLE} ASC", null)

        cursor.moveToFirst()
        if (cursor.count > 0) {
            do {
                val dataPoster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.POSTER_PATH))
                arrayList.add(BitmapFactory.decodeStream(URL(urlPoster+dataPoster).openStream()))
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }
}