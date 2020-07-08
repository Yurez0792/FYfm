package com.futysh.fyfm.di

import com.futysh.fyfm.repository.internal_storage.InternalStorageRepository
import com.futysh.fyfm.repository.internal_storage.InternalStorageRepositoryImpl
import com.futysh.fyfm.repository.network.FmRepository
import com.futysh.fyfm.repository.network.FmRepositoryImpl
import com.futysh.fyfm.repository.network.FmRetrofitService
import com.futysh.fyfm.repository.preferences.PreferenceRepository
import com.futysh.fyfm.repository.preferences.PreferenceRepositoryImpl
import com.futysh.fyfm.repository.room.FmDatabase
import com.futysh.fyfm.repository.room.FmDatabaseImp
import com.futysh.fyfm.view.album_detail.AlbumDetailViewModel
import com.futysh.fyfm.view.home.HomeViewModel
import com.futysh.fyfm.view.sign_in.SignInViewModel
import com.futysh.fyfm.view.sign_up.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FmDatabaseImp(androidContext()) as FmDatabase }
    single { InternalStorageRepositoryImpl(androidContext()) as InternalStorageRepository }
    single { FmRetrofitService() }
    single { FmRepositoryImpl(get()) as FmRepository }
    single { PreferenceRepositoryImpl(get()) as PreferenceRepository }
}

val viewModelModule = module {
    viewModel { SignUpViewModel(androidContext().resources, get(), get(), get()) }
    viewModel { SignInViewModel(androidContext().resources, get(), get()) }
    viewModel { HomeViewModel(androidContext().resources, get(), get(), get(), get()) }
    viewModel { AlbumDetailViewModel(androidContext().resources, get(), get()) }
}