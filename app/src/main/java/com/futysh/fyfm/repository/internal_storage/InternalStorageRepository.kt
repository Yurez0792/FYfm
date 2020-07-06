package com.futysh.fyfm.repository.internal_storage

import android.graphics.Bitmap

interface InternalStorageRepository {

    suspend fun saveToInternalStorage(bitmapImage: Bitmap, userName: String): String

    suspend fun loadImageFromStorage(pathImagePath: String, userName: String): Bitmap?

}