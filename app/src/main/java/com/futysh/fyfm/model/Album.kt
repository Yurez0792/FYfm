package com.futysh.fyfm.model

import android.os.Parcelable
import com.futysh.fyfm.model.last_fm.Artist
import com.futysh.fyfm.model.last_fm.Image
import com.futysh.fyfm.model.last_fm.Rank
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.utils.Constants.Companion.LARGE_ALBUM_IMAGE
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    @SerializedName("name") var name: String,
    @SerializedName("mbid") val mbid: String,
    @SerializedName("url") val url: String,
    @SerializedName("artist") val artist: Artist?,
    @SerializedName("image") val image: List<Image>?,
    @SerializedName("@attr") val rankObj: Rank?
) : BaseAlbum(
    id = null,
    userName = "",
    imageUrl = image?.get(LARGE_ALBUM_IMAGE)?.text,
    albumName = name,
    albumMbid = mbid,
    rank = rankObj?.rank,
    artistUrl = url
), Parcelable {

    fun fillParent() {
        id = null
        userName = ""
        imageUrl = image?.get(LARGE_ALBUM_IMAGE)?.text
        albumName = name
        rank = rankObj?.rank
        artistUrl = url
    }
}