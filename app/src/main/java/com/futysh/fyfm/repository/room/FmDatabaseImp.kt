package com.futysh.fyfm.repository.room

import android.content.Context
import androidx.room.Room

class FmDatabaseImp(context: Context) : FmDatabase {

    companion object {
        private const val DATABASE_NAME = "fm database"
    }

    private lateinit var mFmDatabase: FmRoomDatabase

    init {
        initializeDataBase(context)
    }

    private fun initializeDataBase(context: Context) {
        mFmDatabase = Room.databaseBuilder(
            context,
            FmRoomDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    override fun getFmDatabase(): FmRoomDatabase {
        return mFmDatabase
    }
}