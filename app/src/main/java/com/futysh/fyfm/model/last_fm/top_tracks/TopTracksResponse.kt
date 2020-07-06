package com.futysh.fyfm.model.last_fm.top_tracks

import com.google.gson.annotations.SerializedName

data class TopTracksResponse(
    @SerializedName("toptracks") val topTracks: TopTracks
)