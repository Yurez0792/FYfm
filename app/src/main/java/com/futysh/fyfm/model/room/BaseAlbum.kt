package com.futysh.fyfm.model.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
open class BaseAlbum(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long?,
    @ColumnInfo(name = "user_name") var userName: String?,
    @ColumnInfo(name = "image_url") var imageUrl: String?,
    @ColumnInfo(name = "album_name") var albumName: String?,
    @ColumnInfo(name = "album_mbid") var albumMbid: String?,
    @ColumnInfo(name = "rate") var rank: Int?,
    @ColumnInfo(name = "is_selected") var isSelected: Boolean = true
) : Parcelable
