package com.futysh.fyfm.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.futysh.fyfm.repository.room.model.Favourites

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM favourites WHERE user_name LIKE :userName")
    suspend fun getFavourites(userName: String): List<Favourites>

}