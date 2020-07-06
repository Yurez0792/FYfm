package com.futysh.fyfm.model.last_fm.artist_top_albums

import com.google.gson.annotations.SerializedName

data class TopAlbums(
    @SerializedName("album") val album: List<Album>,
    @SerializedName("@attr") val attr: Attr
)