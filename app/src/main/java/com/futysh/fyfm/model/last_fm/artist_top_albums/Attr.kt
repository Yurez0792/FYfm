package com.futysh.fyfm.model.last_fm.artist_top_albums

import com.google.gson.annotations.SerializedName

data class Attr(
    @SerializedName("artist") val artist: String,
    @SerializedName("page") val page: Int,
    @SerializedName("perPage") val perPage: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("total") val total: Int
)