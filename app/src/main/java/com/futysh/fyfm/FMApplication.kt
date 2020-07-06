package com.futysh.fyfm

import android.app.Application
import com.futysh.fyfm.di.appModule
import com.futysh.fyfm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FMApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FMApplication)
            modules(listOf(appModule, viewModelModule))
        }
    }

}