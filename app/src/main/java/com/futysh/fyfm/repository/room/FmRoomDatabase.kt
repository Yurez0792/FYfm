package com.futysh.fyfm.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.model.room.User
import com.futysh.fyfm.repository.room.dao.FavouritesDao
import com.futysh.fyfm.repository.room.dao.UserDao

@Database(entities = [User::class, BaseAlbum::class], version = 1)
abstract class FmRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun favouritesDao(): FavouritesDao

}