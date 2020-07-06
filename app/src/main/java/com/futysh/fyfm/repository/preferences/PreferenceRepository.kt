package com.futysh.fyfm.repository.preferences

interface PreferenceRepository {

    fun getUserName(): String

    fun setUserName(userName: String)

}