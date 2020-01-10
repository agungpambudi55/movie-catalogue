/*
    Created on Dec 2019
    Agung Pambudi <agung.pambudi5595@gmail.com>
*/

package com.dicoding.picodiploma.appfavorite.item

import android.os.Parcel
import android.os.Parcelable

data class MovieItems(
    var dataId: Int,
    var dataTitle: String?,
    var dataVoteAverage: Double?,
    var dataReleaseDate: String?,
    var dataOverview: String?,
    var dataPosterPath: String?,
    var dataBackdropPath: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(dataId)
        parcel.writeString(dataTitle)
        parcel.writeValue(dataVoteAverage)
        parcel.writeString(dataReleaseDate)
        parcel.writeString(dataOverview)
        parcel.writeString(dataPosterPath)
        parcel.writeString(dataBackdropPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieItems> {
        override fun createFromParcel(parcel: Parcel): MovieItems {
            return MovieItems(parcel)
        }

        override fun newArray(size: Int): Array<MovieItems?> {
            return arrayOfNulls(size)
        }
    }
}