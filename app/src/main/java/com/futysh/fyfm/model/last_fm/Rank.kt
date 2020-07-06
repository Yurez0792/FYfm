package com.futysh.fyfm.model.last_fm

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rank(@SerializedName("rank") val rank: Int) : Parcelable