package com.futysh.fyfm.repository.network

import com.futysh.fyfm.model.last_fm.artist_top_albums.ArtistTopAlbums
import com.futysh.fyfm.model.last_fm.top_albums.TopAlbumsResponse

interface FmRepository {

    suspend fun getTopAlbums(
        method: String,
        tag: String,
        apiKey: String,
        format: String
    ): TopAlbumsResponse?

    suspend fun getArtistTopAlbums(
        method: String,
        artist: String,
        apiKey: String,
        format: String
    ): ArtistTopAlbums?

}