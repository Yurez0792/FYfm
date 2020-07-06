package com.futysh.fyfm.model.last_fm.top_albums

import com.google.gson.annotations.SerializedName

data class TopAlbumsResponse(
    @SerializedName("albums") val albums: Albums
)