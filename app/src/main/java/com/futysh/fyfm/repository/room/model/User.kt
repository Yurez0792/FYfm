package com.futysh.fyfm.repository.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long?,
    @ColumnInfo(name = "user_name") var name: String?,
    @ColumnInfo(name = "email") var email: String?,
    @ColumnInfo(name = "password") var password: String?,
    @ColumnInfo(name = "avatar_path") var avatarPath: String?,
    @ColumnInfo(name = "auth_token") var auth_token: String?
)