package com.futysh.fyfm.view.sign_in

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futysh.fyfm.R
import com.futysh.fyfm.repository.preferences.PreferenceRepository
import com.futysh.fyfm.repository.room.FmDatabase
import com.futysh.fyfm.utils.SingleLiveEvent
import com.futysh.fyfm.view.sign_up.SignUpViewModel.Companion.PASSWORD_LENGTH_STANDARD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SignInViewModel(
    private val resources: Resources,
    private val fmDatabase: FmDatabase,
    private val preferences: PreferenceRepository
) : ViewModel() {

    val mUserAuthorized: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }
    val mWrongEmailLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mPasswordErrorLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mShowProgressLiveData: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent<Unit>()
    }
    val mHideProgressLiveData: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent<Unit>()
    }
    val mDatabaseErrorMessageLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mLogInSuccessLiveData: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent<Unit>()
    }
    val mLogInFailLiveData: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }

    fun signInUser(
        email: String,
        password: String
    ) {
        mShowProgressLiveData.postValue(null)
        if (isEmail(email) && isPasswordComply(password)) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val userDao = fmDatabase.getFmDatabase().userDao()
                        val userByEmail = userDao.getUserByEmail(email)
                        if (userByEmail != null && isPasswordsMatch(password, userByEmail.password)
                        ) {
                            preferences.setUserName(userByEmail.userName)
                            mLogInSuccessLiveData.postValue(null)
                        } else {
                            mLogInFailLiveData.postValue(true)
                        }
                        mHideProgressLiveData.postValue(null)
                    } catch (e: Exception) {
                        Timber.e(e)
                        mDatabaseErrorMessageLiveData.postValue(resources.getString(R.string.database_fetch_error_text))
                        mHideProgressLiveData.postValue(null)
                    }
                }
            }
        }
    }

    fun isUserAuthorized() {
        if (preferences.getUserName().isNotBlank()) {
            mUserAuthorized.postValue(true)
        }
    }

    private fun isPasswordsMatch(inputPassword: String, userPassword: String?): Boolean {
        return if (inputPassword == userPassword) {
            true
        } else {
            mHideProgressLiveData.postValue(null)
            false
        }
    }

    private fun isPasswordComply(password: String): Boolean {
        return if (password.length > PASSWORD_LENGTH_STANDARD) {
            true
        } else {
            setPasswordErrorMessage(resources.getString(R.string.password_error_text))
            mHideProgressLiveData.postValue(null)
            false
        }
    }

    private fun isEmail(email: String): Boolean {
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return if (isEmail) {
            true
        } else {
            setWrongEmailMessage(resources.getString(R.string.wrong_email_text))
            mHideProgressLiveData.postValue(null)
            false
        }
    }

    private fun setWrongEmailMessage(message: String) {
        mWrongEmailLiveData.postValue(message)
    }

    private fun setPasswordErrorMessage(message: String) {
        mPasswordErrorLiveData.postValue(message)
    }
}