package com.futysh.fyfm.repository.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FmRetrofitService {

    companion object {
        private const val FM_BASE_URL = "http://ws.audioscrobbler.com/"
        const val FM_TOP_ALBUMS_METHOD = "tag.gettopalbums"
        const val FM_TOP_ARTISTS_TAG = "disco"
        const val FM_API_KEY = "16d906c812cd03992b1eae2656d8d69a"
        const val FM_FORMAT = "json"
    }

    private var fmRetrofit: Retrofit? = null

    fun getFmRetrofit(): Retrofit {
        return if (fmRetrofit != null) {
            fmRetrofit!!
        } else {
            fmRetrofit = Retrofit.Builder()
                .baseUrl(FM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                    CoroutineCallAdapterFactory()
                )
                .build()
            fmRetrofit!!
        }
    }
}