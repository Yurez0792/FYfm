package com.futysh.fyfm.view.sign_up

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.futysh.fyfm.MainActivity
import com.futysh.fyfm.R
import com.futysh.fyfm.databinding.SignUpFragmentLayoutBinding
import com.futysh.fyfm.utils.Constants.Companion.EMAIL
import com.futysh.fyfm.utils.Constants.Companion.PASSWORD
import com.futysh.fyfm.utils.Constants.Companion.USER_NAME
import com.futysh.fyfm.view.CustomTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject


class SignUpFragment : Fragment() {

    companion object {
        private const val IMAGE_CATALOG = "image/*"
        private const val CONFIRM_PASSWORD = "confirm password"
        private const val AVATAR = "avatar"
        private const val IMAGES_REQUEST_CODE = 101
    }

    private val mViewModel by inject<SignUpViewModel>()
    private var mAvatar: Bitmap? = null
    private lateinit var mBinding: SignUpFragmentLayoutBinding
    private lateinit var mEmailErrorText: TextView
    private lateinit var mUserNameErrorText: TextView
    private lateinit var mConfirmPasswordErrorText: TextView
    private lateinit var mPasswordErrorText: TextView
    private lateinit var mUserNameInputLayout: TextInputLayout
    private lateinit var mConfirmPasswordInputLayout: TextInputLayout
    private lateinit var mPasswordInputLayout: TextInputLayout
    private lateinit var mEmailInputLayout: TextInputLayout
    private lateinit var mUserNameEdit: TextInputEditText
    private lateinit var mConfirmPasswordEdit: TextInputEditText
    private lateinit var mPasswordEdit: TextInputEditText
    private lateinit var mEmailEdit: TextInputEditText
    private lateinit var mPhotoImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = SignUpFragmentLayoutBinding.inflate(inflater)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEmailErrorText = mBinding.emailErrorText
        mEmailInputLayout = mBinding.emailInputLayout
        mEmailEdit = mBinding.emailEdit
        mUserNameErrorText = mBinding.userNameErrorText
        mConfirmPasswordErrorText = mBinding.confirmPasswordErrorText
        mUserNameInputLayout = mBinding.userNameInputLayout
        mConfirmPasswordInputLayout = mBinding.confirmPasswordInputLayout
        mUserNameEdit = mBinding.userNameEdit
        mConfirmPasswordEdit = mBinding.confirmPasswordEdit
        mPasswordEdit = mBinding.passwordEdit
        mPasswordErrorText = mBinding.passwordErrorText
        mPasswordInputLayout = mBinding.passwordInputLayout
        mPhotoImageView = mBinding.photoImageView

        initListeners()
        subscribeToLiveData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            mEmailEdit.setText(savedInstanceState.getString(EMAIL))
            mPasswordEdit.setText(savedInstanceState.getString(PASSWORD))
            mConfirmPasswordEdit.setText(savedInstanceState.getString(CONFIRM_PASSWORD))
            mAvatar = savedInstanceState.getParcelable(AVATAR)
            mPhotoImageView.background =
                resources.getDrawable(R.drawable.ic_photo_holder_background_transparent, null)
            mPhotoImageView.setImageBitmap(mAvatar)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGES_REQUEST_CODE && resultCode == RESULT_OK) {
            val chosenImageUri = data?.data
            if (resultCode == RESULT_OK && chosenImageUri != null) {

                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    mAvatar = ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            context?.contentResolver!!,
                            chosenImageUri
                        )
                    )
                } else {
                    mAvatar =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, chosenImageUri)
                }

                mPhotoImageView.background =
                    resources.getDrawable(R.drawable.ic_photo_holder_background_transparent, null)
                mPhotoImageView.setImageBitmap(mAvatar)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EMAIL, mEmailEdit.text.toString())
        outState.putString(USER_NAME, mUserNameEdit.text.toString())
        outState.putString(PASSWORD, mPasswordEdit.text.toString())
        outState.putString(CONFIRM_PASSWORD, mConfirmPasswordEdit.text.toString())
        outState.putParcelable(AVATAR, mAvatar)
    }

    private fun subscribeToLiveData() {
        mViewModel.mWrongEmailLiveData.observe(this, Observer {
            it?.let {
                mEmailErrorText.text = it
                mEmailErrorText.visibility = View.VISIBLE
                mEmailInputLayout.background =
                    resources.getDrawable(R.drawable.error_input_background, null)
                mEmailEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_mail_red,
                        null
                    ), null, null, null
                )
            }
        })

        mViewModel.mUserNameErrorLiveData.observe(this, Observer {
            it?.let {
                mUserNameErrorText.text = it
                mUserNameErrorText.visibility = View.VISIBLE
                mUserNameInputLayout.background =
                    resources.getDrawable(R.drawable.error_input_background, null)
                mUserNameEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_user_red,
                        null
                    ), null, null, null
                )
            }
        })

        mViewModel.mPasswordErrorLiveData.observe(this, Observer {
            it?.let {
                mPasswordErrorText.text = it
                mPasswordErrorText.visibility = View.VISIBLE
                mPasswordInputLayout.background =
                    resources.getDrawable(R.drawable.error_input_background, null)
                mPasswordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_password_red,
                        null
                    ), null, null, null
                )
            }
        })

        mViewModel.mConfirmPasswordLiveData.observe(this, Observer {
            it?.let {
                mConfirmPasswordErrorText.text = it
                mConfirmPasswordErrorText.visibility = View.VISIBLE
                mConfirmPasswordInputLayout.background =
                    resources.getDrawable(R.drawable.error_input_background, null)
                mConfirmPasswordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_password_red,
                        null
                    ), null, null, null
                )
            }
        })

        mViewModel.mGeneralErrorMessageLiveData.observe(this, Observer {
            it?.let {
                (activity as MainActivity).showErrorNotification(it)
            }
        })

        mViewModel.mUserRegisteredLiveData.observe(this, Observer {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_signUpFragment_to_homeFragment)
        })

        mViewModel.mShowProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.VISIBLE
        })

        mViewModel.mHideProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.GONE
        })
    }

    private fun initListeners() {
        mPhotoImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = IMAGE_CATALOG
            startActivityForResult(intent, IMAGES_REQUEST_CODE)
        }

        mBinding.signUpButton.setOnClickListener {
            mViewModel.signUpUser(
                mEmailEdit.text.toString(),
                mUserNameEdit.text.toString(),
                mPasswordEdit.text.toString(),
                mConfirmPasswordEdit.text.toString(),
                mAvatar
            )
        }

        mBinding.signInButton.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        mEmailEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mEmailErrorText.visibility = View.GONE
                mEmailInputLayout.background =
                    resources.getDrawable(R.drawable.input_background, null)
                mEmailEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_mail,
                        null
                    ), null, null, null
                )
            }
        })

        mUserNameEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mUserNameErrorText.visibility = View.GONE
                mUserNameInputLayout.background =
                    resources.getDrawable(R.drawable.input_background, null)
                mUserNameEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_user,
                        null
                    ), null, null, null
                )
            }
        })

        mPasswordEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mPasswordErrorText.visibility = View.GONE
                mPasswordInputLayout.background =
                    resources.getDrawable(R.drawable.input_background, null)
                mPasswordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_password,
                        null
                    ), null, null, null
                )
            }
        })

        mConfirmPasswordEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mConfirmPasswordErrorText.visibility = View.GONE
                mConfirmPasswordInputLayout.background =
                    resources.getDrawable(R.drawable.input_background, null)
                mConfirmPasswordEdit.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_password,
                        null
                    ), null, null, null
                )
            }
        })
    }
}