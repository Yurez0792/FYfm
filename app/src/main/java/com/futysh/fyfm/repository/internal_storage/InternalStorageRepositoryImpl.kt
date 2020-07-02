package com.futysh.fyfm.repository.internal_storage

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import timber.log.Timber
import java.io.*


class InternalStorageRepositoryImpl(private val context: Context) : InternalStorageRepository {

    companion object {
        private const val PHOTO_DIRECTORY = "photo_directory"
        private const val IMAGE_FORMAT = ".jpg"
    }

    override suspend fun saveToInternalStorage(bitmapImage: Bitmap, userName: String): String {
        val directory: File =
            ContextWrapper(context).getDir(PHOTO_DIRECTORY, Context.MODE_PRIVATE)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(File(directory, "$userName + $IMAGE_FORMAT"))
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
        return directory.absolutePath
    }

    override suspend fun loadImageFromStorage(pathImagePath: String, userName: String): Bitmap? {
        return try {
            val file = File(pathImagePath, "$userName + $IMAGE_FORMAT")
            BitmapFactory.decodeStream(FileInputStream(file))
        } catch (e: FileNotFoundException) {
            Timber.e(e)
            null
        }
    }
}