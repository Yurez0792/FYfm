package com.futysh.fyfm.repository.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favourites(
    @PrimaryKey @ColumnInfo var id: Int,
    @ColumnInfo(name = "user_name") var name: String?
)