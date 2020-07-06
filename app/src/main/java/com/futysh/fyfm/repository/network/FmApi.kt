package com.futysh.fyfm.repository.network

import com.futysh.fyfm.model.last_fm.artist_top_albums.ArtistTopAlbums
import com.futysh.fyfm.model.last_fm.top_albums.TopAlbumsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FmApi {

    @GET("2.0/")
    suspend fun getTopAlbums(
        @Query("method") method: String,
        @Query("tag") tag: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): Response<TopAlbumsResponse>

    @GET("2.0/")
    suspend fun getTopTracks(
        @Query("method") method: String,
        @Query("artist") tag: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): Response<ArtistTopAlbums>
}