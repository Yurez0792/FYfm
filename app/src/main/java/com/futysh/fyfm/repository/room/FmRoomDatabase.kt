package com.futysh.fyfm.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.futysh.fyfm.repository.room.dao.FavouritesDao
import com.futysh.fyfm.repository.room.dao.UserDao
import com.futysh.fyfm.repository.room.model.Favourites
import com.futysh.fyfm.repository.room.model.User

@Database(entities = [User::class, Favourites::class], version = 1)
abstract class FmRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun favouritesDao(): FavouritesDao

}