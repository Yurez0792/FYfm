package com.futysh.fyfm.repository.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.futysh.fyfm.model.room.User

@Dao
interface UserDao {

    @Insert
    suspend fun saveUser(user: User): Long

    @Query(value = "SELECT * FROM user WHERE email LIKE :email")
    suspend fun getUserByEmail(email: String): User?

    @Query(value = "SELECT * FROM user WHERE user_name LIKE :userName")
    suspend fun getUserByName(userName: String): User?

    @Query(value = "SELECT * FROM user WHERE user_name LIKE :userName")
    suspend fun getUserByUsername(userName: String): User?

}