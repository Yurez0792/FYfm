package com.futysh.fyfm.view.sign_up

import android.content.res.Resources
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futysh.fyfm.R
import com.futysh.fyfm.model.room.User
import com.futysh.fyfm.repository.internal_storage.InternalStorageRepository
import com.futysh.fyfm.repository.preferences.PreferenceRepository
import com.futysh.fyfm.repository.room.FmDatabase
import com.futysh.fyfm.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SignUpViewModel(
    private val resources: Resources,
    private val fmDatabase: FmDatabase,
    private val internalStorageRepository: InternalStorageRepository,
    private val preferences: PreferenceRepository
) : ViewModel() {

    companion object {
        const val USERNAME_LENGTH_STANDARD = 1
        const val PASSWORD_LENGTH_STANDARD = 7
    }

    val mWrongEmailLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mUserNameErrorLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mPasswordErrorLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mConfirmPasswordLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mShowProgressLiveData: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent<Unit>()
    }
    val mHideProgressLiveData: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent<Unit>()
    }
    val mGeneralErrorMessageLiveData: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }
    val mUserRegisteredLiveData: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent<Unit>()
    }

    fun signUpUser(
        email: String,
        userName: String,
        password: String,
        confirmPassword: String,
        bitmap: Bitmap?
    ) {
        mShowProgressLiveData.postValue(null)
        if (
            !isBitmapNull(bitmap)
            && isUserNameComply(userName)
            && isEmail(email)
            && isPasswordComply(password)
            && isPasswordsMatch(password, confirmPassword)
        ) {
            checkIfUserAuthorized(email, userName, password, bitmap!!)
        }
    }

    private fun isBitmapNull(bitmap: Bitmap?): Boolean {
        return if (bitmap == null) {
            postError(resources.getString(R.string.choose_avatar_text))
            mHideProgressLiveData.postValue(null)
            true
        } else {
            false
        }
    }

    private fun postError(errorMessage: String) {
        mGeneralErrorMessageLiveData.postValue(resources.getString(R.string.choose_avatar_text))
    }

    private fun checkIfUserAuthorized(
        email: String,
        userName: String,
        password: String,
        bitmap: Bitmap
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val userDao = fmDatabase.getFmDatabase().userDao()
                    val userByEmail = userDao.getUserByEmail(email)
                    val userByUsername = userDao.getUserByUsername(userName)
                    if (userByEmail == null && userByUsername == null) {
                        val avatarPath =
                            internalStorageRepository.saveToInternalStorage(bitmap, userName)
                        signUpUser(email, userName, password, avatarPath)
                    } else {
                        notifyEmailRegistered(userByEmail)
                        notifyUsernameRegistered(userByUsername)
                        mHideProgressLiveData.postValue(null)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    postError(resources.getString(R.string.database_fetch_error_text))
                    mHideProgressLiveData.postValue(null)
                }
            }
        }
    }

    private fun isPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return if (password == confirmPassword) {
            true
        } else {
            setConfirmPasswordErrorMessage(resources.getString(R.string.passwords_do_not_match_error_text))
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

    private fun isUserNameComply(userName: String): Boolean {
        return if (isUserNameLengthEnough(userName)) {
            true
        } else {
            mHideProgressLiveData.postValue(null)
            false
        }
    }

    private fun notifyUsernameRegistered(userByUsername: User?) {
        if (userByUsername != null) {
            setUserNameErrorMessage(resources.getString(R.string.username_registered_text))
        }
    }

    private fun notifyEmailRegistered(userByEmail: User?) {
        if (userByEmail != null) {
            setWrongEmailMessage(resources.getString(R.string.email_registered_text))
        }
    }

    private fun signUpUser(email: String, userName: String, password: String, avatarPath: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val rowId = fmDatabase.getFmDatabase().userDao().saveUser(
                        User(
                            id = null,
                            userName = userName,
                            email = email,
                            password = password,
                            avatarPath = avatarPath,
                            auth_token = ""
                        )
                    )

                    if (rowId < 0) {
                        postError(resources.getString(R.string.sign_up_error_occurred_text))
                    } else {
                        preferences.setUserName(userName)
                        mUserRegisteredLiveData.postValue(null)
                        mHideProgressLiveData.postValue(null)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    mHideProgressLiveData.postValue(null)
                }
            }
        }
    }

    private fun isUserNameLengthEnough(userName: String): Boolean {
        return if (userName.length > USERNAME_LENGTH_STANDARD) {
            true
        } else {
            setUserNameErrorMessage(resources.getString(R.string.user_name_error_text))
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

    private fun setUserNameErrorMessage(message: String) {
        mUserNameErrorLiveData.postValue(message)
    }

    private fun setPasswordErrorMessage(message: String) {
        mPasswordErrorLiveData.postValue(message)
    }

    private fun setConfirmPasswordErrorMessage(message: String) {
        mConfirmPasswordLiveData.postValue(message)
    }
}