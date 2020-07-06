package com.futysh.fyfm.model.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long?,
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String?,
    @ColumnInfo(name = "avatar_path") var avatarPath: String?,
    @ColumnInfo(name = "auth_token") var auth_token: String?
) : Parcelable