package com.futysh.fyfm.model.last_fm.artist_top_albums

import com.futysh.fyfm.model.last_fm.Artist
import com.futysh.fyfm.model.last_fm.Image
import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("name") val name: String,
    @SerializedName("playcount") val playcount: Int,
    @SerializedName("mbid") val mbid: String,
    @SerializedName("url") val url: String,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("image") val image: List<Image>
)