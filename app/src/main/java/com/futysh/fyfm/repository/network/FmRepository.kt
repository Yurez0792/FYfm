package com.futysh.fyfm.repository.network

import com.futysh.fyfm.model.last_fm.top_albums.TopAlbumsResponse
import com.futysh.fyfm.model.last_fm.top_tracks.TopTracksResponse

interface FmRepository {

    suspend fun getTopAlbums(
        method: String,
        tag: String,
        apiKey: String,
        format: String
    ): TopAlbumsResponse?

    suspend fun getTopTracks(
        method: String,
        artist: String,
        apiKey: String,
        format: String
    ): TopTracksResponse?

}