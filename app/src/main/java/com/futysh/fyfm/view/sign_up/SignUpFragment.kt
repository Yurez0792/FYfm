package com.futysh.fyfm.view.sign_up

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.futysh.fyfm.MainActivity
import com.futysh.fyfm.databinding.SignUpFragmentLayoutBinding
import com.futysh.fyfm.view.CustomTextWatcher
import org.koin.android.ext.android.inject


class SignUpFragment : Fragment() {

    private val mViewModel by inject<SignUpViewModel>()
    private var mBitmap: Bitmap? = null
    private lateinit var mBinding: SignUpFragmentLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.mWrongEmailLiveData.observe(this, Observer {
            it?.let {
                mBinding.emailErrorText.text = it
                mBinding.emailErrorText.visibility = View.VISIBLE
            }
        })

        mViewModel.mUserNameErrorLiveData.observe(this, Observer {
            it?.let {
                mBinding.userNameErrorText.text = it
                mBinding.userNameErrorText.visibility = View.VISIBLE
            }
        })

        mViewModel.mPasswordErrorLiveData.observe(this, Observer {
            it?.let {
                mBinding.passwordErrorText.text = it
                mBinding.passwordErrorText.visibility = View.VISIBLE
            }
        })

        mViewModel.mConfirmPasswordLiveData.observe(this, Observer {
            it?.let {
                mBinding.confirmPasswordErrorText.text = it
                mBinding.confirmPasswordErrorText.visibility = View.VISIBLE
            }
        })

        mViewModel.mGeneralErrorMessageLiveData.observe(this, Observer {
            it?.let {
                (activity as MainActivity).showErrorNotification(it)
            }
        })

        mViewModel.mUserRegisteredLiveData.observe(this, Observer {
//            todo: navigate to main
            Toast.makeText(context, "User registered", Toast.LENGTH_LONG).show()
        })

        mViewModel.mShowProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.VISIBLE
        })

        mViewModel.mHideProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.GONE
        })
    }

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

        initListeners()
    }

    private fun initListeners() {

        val emailEdit = mBinding.emailEdit
        val userNameEdit = mBinding.userNameEdit
        val passwordEdit = mBinding.passwordEdit
        val confirmPasswordEdit = mBinding.confirmPasswordEdit

        mBinding.photoImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        mBinding.signUpButton.setOnClickListener {
            mViewModel.signUpUser(
                emailEdit.text.toString(),
                userNameEdit.text.toString(),
                passwordEdit.text.toString(),
                confirmPasswordEdit.text.toString(),
                mBitmap
            )
        }

        emailEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mBinding.emailErrorText.visibility = View.GONE
            }
        })

        userNameEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mBinding.userNameErrorText.visibility = View.GONE
            }
        })

        passwordEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mBinding.passwordErrorText.visibility = View.GONE
            }
        })

        confirmPasswordEdit.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mBinding.confirmPasswordErrorText.visibility = View.GONE
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val chosenImageUri = data?.data
            if (resultCode == RESULT_OK && chosenImageUri != null) {
                mBitmap =
                    MediaStore.Images.Media.getBitmap(context?.contentResolver, chosenImageUri)
                mBinding.photoImageView.setImageBitmap(mBitmap)
            }
        }
    }
}