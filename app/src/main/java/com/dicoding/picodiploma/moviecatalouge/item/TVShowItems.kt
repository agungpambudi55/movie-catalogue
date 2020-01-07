package com.dicoding.picodiploma.moviecatalouge.item

import android.os.Parcel
import android.os.Parcelable

data class TVShowItems(
    var dataId: Int,
    var dataName: String?,
    var dataVoteAverage: Double?,
    var dataFirstAirDate: String?,
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
        parcel.writeString(dataName)
        parcel.writeValue(dataVoteAverage)
        parcel.writeString(dataFirstAirDate)
        parcel.writeString(dataOverview)
        parcel.writeString(dataPosterPath)
        parcel.writeString(dataBackdropPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TVShowItems> {
        override fun createFromParcel(parcel: Parcel): TVShowItems {
            return TVShowItems(
                parcel
            )
        }

        override fun newArray(size: Int): Array<TVShowItems?> {
            return arrayOfNulls(size)
        }
    }
}