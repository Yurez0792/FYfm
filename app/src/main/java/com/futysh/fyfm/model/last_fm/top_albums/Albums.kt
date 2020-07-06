package com.futysh.fyfm.model.last_fm.top_albums

import com.futysh.fyfm.model.Album
import com.google.gson.annotations.SerializedName

data class Albums(
    @SerializedName("album") val album: List<Album>
)