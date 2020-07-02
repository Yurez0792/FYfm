package com.futysh.fyfm.di

import com.futysh.fyfm.repository.internal_storage.InternalStorageRepository
import com.futysh.fyfm.repository.internal_storage.InternalStorageRepositoryImpl
import com.futysh.fyfm.repository.room.FmDatabaseImp
import com.futysh.fyfm.view.sign_up.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FmDatabaseImp(androidContext()) }
    single { InternalStorageRepositoryImpl(androidContext()) as InternalStorageRepository }
}

val viewModelModule = module {
    viewModel { SignUpViewModel(androidContext().resources, get(), get()) }
}