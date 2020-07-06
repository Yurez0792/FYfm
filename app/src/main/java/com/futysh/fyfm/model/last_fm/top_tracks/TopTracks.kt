package com.futysh.fyfm.model.last_fm.top_tracks

import com.google.gson.annotations.SerializedName

data class TopTracks(
    @SerializedName("track") val track: List<Track>
)