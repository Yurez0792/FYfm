package com.futysh.fyfm.repository.preferences

import android.content.Context

class PreferenceRepositoryImpl(context: Context) : PreferenceRepository {

    private val mPreferences = context.getSharedPreferences(
        PreferenceRepositoryImpl::class.java.name,
        Context.MODE_PRIVATE
    )
    private val mEditPreferences = mPreferences.edit()

    companion object {
        const val USER_NAME = "user_name"
    }

    override fun getUserName(): String {
        return mPreferences.getString(USER_NAME, "").orEmpty()
    }

    override fun setUserName(name: String) {
        mEditPreferences.putString(USER_NAME, name).apply()
    }
}