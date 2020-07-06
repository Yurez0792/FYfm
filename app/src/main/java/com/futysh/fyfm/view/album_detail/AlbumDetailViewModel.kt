package com.futysh.fyfm.view.album_detail

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futysh.fyfm.R
import com.futysh.fyfm.model.last_fm.artist_top_albums.Album
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.repository.network.FmRepository
import com.futysh.fyfm.repository.room.FmDatabase
import com.futysh.fyfm.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AlbumDetailViewModel(
    private val resources: Resources,
    private val fmDatabase: FmDatabase,
    private val fmRepository: FmRepository
) : ViewModel() {

    val mDatabaseErrorMessageLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mFavouriteAlbumLiveData: SingleLiveEvent<BaseAlbum> by lazy {
        SingleLiveEvent<BaseAlbum>()
    }
    val mArtistAlbumLiveData: SingleLiveEvent<List<Album>> by lazy {
        SingleLiveEvent<List<Album>>()
    }
    val mShowProgressLiveData: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }
    val mHideProgressLiveData: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }

    fun getFavouriteFromDB(album: BaseAlbum) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val favouriteAlbum = fmDatabase.getFmDatabase().favouritesDao()
                        .getFavouriteAlbum(album.userName!!, album.albumName!!)
                    mFavouriteAlbumLiveData.postValue(favouriteAlbum)
                } catch (exception: Exception) {
                    Timber.e(exception)
                    mDatabaseErrorMessageLiveData.postValue(resources.getString(R.string.database_fetch_error_text))
                }
            }
        }
    }

    fun processByDatabase(album: BaseAlbum) {
        if (album.isSelected) {
            saveAlbumToFavourites(album)
        } else {
            removeAlbumFromFavourites(album)
        }
    }

    private fun saveAlbumToFavourites(album: BaseAlbum) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val saveAlbum = fmDatabase.getFmDatabase().favouritesDao().saveAlbum(album)
                    if (saveAlbum < 1) {
                        mDatabaseErrorMessageLiveData.postValue(resources.getString(R.string.database_save_error_text))
                    }
                } catch (exception: Exception) {
                    Timber.e(exception)
                    mDatabaseErrorMessageLiveData.postValue(resources.getString(R.string.database_save_error_text))
                }
            }
        }
    }

    private fun removeAlbumFromFavourites(album: BaseAlbum) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    fmDatabase.getFmDatabase().favouritesDao().removeAlbum(album)
                } catch (exception: Exception) {
                    Timber.e(exception)
                    mDatabaseErrorMessageLiveData.postValue(resources.getString(R.string.database_save_error_text))
                }
            }
        }
    }

    fun getTopTracks(
        fmTopTracksMethod: String,
        fmApiKey: String,
        fmFormat: String,
        album: BaseAlbum
    ) {
        showProgress()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val artistName = getArtistName(album)
                    artistName?.let {
                        mArtistAlbumLiveData.postValue(
                            fmRepository.getArtistTopAlbums(
                                fmTopTracksMethod,
                                it,
                                fmApiKey,
                                fmFormat
                            )?.topAlbums?.album
                        )
                    }
                    hideProgress()
                } catch (exception: Exception) {
                    Timber.e(exception)
                    mDatabaseErrorMessageLiveData.postValue(resources.getString(R.string.fetching_tracks_error_text))
                    hideProgress()
                }
            }
        }
    }

    private fun getArtistName(album: BaseAlbum): String? {
        var artistName: String? = null
        val url = album.artistUrl?.split("/")
        url?.size?.let {
            artistName = url[it.minus(1)]
        }

        return artistName
    }

    private fun showProgress() {
        mShowProgressLiveData.postValue(true)
    }

    private fun hideProgress() {
        mHideProgressLiveData.postValue(true)
    }
}