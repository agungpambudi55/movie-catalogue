package com.dicoding.picodiploma.moviecatalouge.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.picodiploma.moviecatalouge.database.DatabaseContract

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "db_movie_catalouge"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_MOVIE_FAVORITE = "CREATE TABLE ${DatabaseContract.MovieColumns.TABLE_NAME} " +
                "("+
                    "${DatabaseContract.MovieColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DatabaseContract.MovieColumns.TITLE} TEXT NOT NULL, " +
                    "${DatabaseContract.MovieColumns.VOTE_AVERAGE} TEXT NOT NULL, " +
                    "${DatabaseContract.MovieColumns.RELEASE_DATE} TEXT NOT NULL, " +
                    "${DatabaseContract.MovieColumns.OVERVIEW} TEXT NOT NULL, " +
                    "${DatabaseContract.MovieColumns.POSTER_PATH} TEXT NOT NULL, " +
                    "${DatabaseContract.MovieColumns.BACKDROP_PATH} TEXT NOT NULL" +
                ")"

        private const val SQL_CREATE_TABLE_TVSHOW_FAVORITE = "CREATE TABLE ${DatabaseContract.TVShowColumns.TABLE_NAME} " +
                "("+
                    "${DatabaseContract.TVShowColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DatabaseContract.TVShowColumns.NAME} TEXT NOT NULL, " +
                    "${DatabaseContract.TVShowColumns.VOTE_AVERAGE} TEXT NOT NULL, " +
                    "${DatabaseContract.TVShowColumns.FIRST_AIR_DATE} TEXT NOT NULL, " +
                    "${DatabaseContract.TVShowColumns.OVERVIEW} TEXT NOT NULL, " +
                    "${DatabaseContract.TVShowColumns.POSTER_PATH} TEXT NOT NULL, " +
                    "${DatabaseContract.TVShowColumns.BACKDROP_PATH} TEXT NOT NULL" +
                ")"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE_FAVORITE)
        db.execSQL(SQL_CREATE_TABLE_TVSHOW_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.TVShowColumns.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.MovieColumns.TABLE_NAME}")
        onCreate(db)
    }
}