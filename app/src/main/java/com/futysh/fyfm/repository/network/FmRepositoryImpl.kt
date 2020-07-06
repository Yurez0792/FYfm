package com.futysh.fyfm.repository.network

import com.futysh.fyfm.model.last_fm.top_albums.TopAlbumsResponse
import com.futysh.fyfm.model.last_fm.top_tracks.TopTracksResponse

class FmRepositoryImpl(fmRetrofitService: FmRetrofitService) : BaseFmRepository(),
    FmRepository {

    private val fmApi = fmRetrofitService.getFmRetrofit().create(FmApi::class.java)

    override suspend fun getTopAlbums(
        method: String,
        tag: String,
        apiKey: String,
        format: String
    ): TopAlbumsResponse? {
        return safeApiCall(
            call = {
                fmApi.getTopAlbums(method, tag, apiKey, format)
            },
            errorMessage = "Error Fetching Top Artists"
        )
    }

    override suspend fun getTopTracks(
        method: String,
        artist: String,
        apiKey: String,
        format: String
    ): TopTracksResponse? {
        return safeApiCall(
            call = {
                fmApi.getTopTracks(method, artist, apiKey, format)
            },
            errorMessage = "Error Fetching Top Tracks"
        )
    }
}