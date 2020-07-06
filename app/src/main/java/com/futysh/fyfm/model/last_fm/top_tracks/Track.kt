package com.futysh.fyfm.model.last_fm.top_tracks

import com.futysh.fyfm.model.last_fm.Artist
import com.futysh.fyfm.model.last_fm.Image
import com.futysh.fyfm.model.last_fm.Rank
import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("name") val name: String,
    @SerializedName("playcount") val playcount: Int,
    @SerializedName("listeners") val listeners: Int,
    @SerializedName("mbid") val mbid: String,
    @SerializedName("url") val url: String,
    @SerializedName("streamable") val streamable: Int,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("@attr") val rank: Rank
)