package com.futysh.fyfm.repository.room.dao

import androidx.room.*
import com.futysh.fyfm.model.room.BaseAlbum

@Dao
interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlbum(baseAlbum: BaseAlbum): Long

    @Delete
    suspend fun removeAlbum(baseAlbum: BaseAlbum)

    @Query(value = "SELECT * FROM baseAlbum WHERE user_name LIKE :userName")
    suspend fun getFavouriteAlbums(userName: String): List<BaseAlbum>

    @Query(value = "SELECT * FROM baseAlbum WHERE user_name LIKE :userName and album_name LIKE :albumName")
    suspend fun getFavouriteAlbum(userName: String, albumName: String): BaseAlbum

}