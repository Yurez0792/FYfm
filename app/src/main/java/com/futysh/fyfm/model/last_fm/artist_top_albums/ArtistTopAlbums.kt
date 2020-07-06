package com.futysh.fyfm.model.last_fm.artist_top_albums

import com.google.gson.annotations.SerializedName

data class ArtistTopAlbums(
    @SerializedName("topalbums") val topAlbums: TopAlbums
)