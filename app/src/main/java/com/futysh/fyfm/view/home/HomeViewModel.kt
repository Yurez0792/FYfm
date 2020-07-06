package com.futysh.fyfm.view.home

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futysh.fyfm.R
import com.futysh.fyfm.model.Album
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.model.room.User
import com.futysh.fyfm.repository.internal_storage.InternalStorageRepository
import com.futysh.fyfm.repository.network.FmRepository
import com.futysh.fyfm.repository.network.FmRetrofitService.Companion.FM_API_KEY
import com.futysh.fyfm.repository.network.FmRetrofitService.Companion.FM_FORMAT
import com.futysh.fyfm.repository.network.FmRetrofitService.Companion.FM_TOP_ALBUMS_METHOD
import com.futysh.fyfm.repository.network.FmRetrofitService.Companion.FM_TOP_ARTISTS_TAG
import com.futysh.fyfm.repository.preferences.PreferenceRepository
import com.futysh.fyfm.repository.room.FmDatabase
import com.futysh.fyfm.utils.SingleLiveEvent
import com.futysh.fyfm.utils.multipleLet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel(
    private val resources: Resources,
    private val fmDatabase: FmDatabase,
    private val internalStorageRepository: InternalStorageRepository,
    private val fmRepository: FmRepository,
    private val preferences: PreferenceRepository
) : ViewModel() {

    val mAlbumsLiveData: SingleLiveEvent<List<Album>> by lazy {
        SingleLiveEvent<List<Album>>()
    }
    val mFavouritesAlbumsLiveData: SingleLiveEvent<List<BaseAlbum>> by lazy {
        SingleLiveEvent<List<BaseAlbum>>()
    }
    val mProfileIconLiveData: SingleLiveEvent<Drawable> by lazy {
        SingleLiveEvent<Drawable>()
    }
    val mUserLiveData: SingleLiveEvent<User> by lazy {
        SingleLiveEvent<User>()
    }
    val mShowProgressLiveData: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }
    val mHideProgressLiveData: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }
    val mGeneralErrorMessageLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mIsLoggedOut: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent<Unit>()
    }

    fun getUserFromDB() {
        showProgress()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val user = fmDatabase.getFmDatabase().userDao()
                        .getUserByName(preferences.getUserName())
                    mUserLiveData.postValue(user)
                    getAvatar(user?.avatarPath, user?.userName)
                    fetchContent(user)
                } catch (e: Exception) {
                    Timber.e(e)
                    hideProgress()
                }
            }
        }
    }

    fun logOut() {
        showProgress()
        preferences.setUserName("")
        mIsLoggedOut.call()
        hideProgress()
    }

    private fun showProgress() {
        mShowProgressLiveData.postValue(true)
    }

    private fun hideProgress() {
        mHideProgressLiveData.postValue(true)
    }

    private fun getAvatar(path: String?, userName: String?) {
        multipleLet(path, userName) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        mProfileIconLiveData.postValue(
                            getCircularDrawable(
                                internalStorageRepository.loadImageFromStorage(
                                    path!!,
                                    userName!!
                                )
                            )
                        )
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    private fun getCircularDrawable(bitmap: Bitmap?): Drawable {
        val drawable: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(resources, bitmap)
        drawable.isCircular = true

        return drawable
    }

    private fun fetchContent(mUser: User?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val favourites =
                        fmDatabase.getFmDatabase().favouritesDao()
                            .getFavouriteAlbums(mUser?.userName!!)

                    val albums = fmRepository.getTopAlbums(
                        FM_TOP_ALBUMS_METHOD,
                        FM_TOP_ARTISTS_TAG,
                        FM_API_KEY,
                        FM_FORMAT
                    )?.albums?.album

                    if (favourites.isNotEmpty()) {
                        mFavouritesAlbumsLiveData.postValue(favourites)
                    }
                    albums.let {
                        mAlbumsLiveData.postValue(it)
                    }
                    hideProgress()
                } catch (e: Exception) {
                    Timber.e(e)
                    mGeneralErrorMessageLiveData.postValue(resources.getString(R.string.data_fetching_error_text))
                    hideProgress()
                }
            }
        }
    }
}