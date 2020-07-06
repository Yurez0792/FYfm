package com.futysh.fyfm.view.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.futysh.fyfm.MainActivity
import com.futysh.fyfm.R
import com.futysh.fyfm.databinding.SignInFragmentLayoutBinding
import com.futysh.fyfm.utils.Constants.Companion.EMAIL
import com.futysh.fyfm.utils.Constants.Companion.PASSWORD
import com.futysh.fyfm.view.CustomTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject

class SignInFragment : Fragment() {

    private lateinit var mBinding: SignInFragmentLayoutBinding
    private val mViewModel by inject<SignInViewModel>()
    private lateinit var mEmailErrorText: TextView
    private lateinit var mPasswordErrorText: TextView
    private lateinit var mPasswordInputLayout: TextInputLayout
    private lateinit var mEmailInputLayout: TextInputLayout
    private lateinit var mPasswordEdit: TextInputEditText
    private lateinit var mEmailEdit: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = SignInFragmentLayoutBinding.inflate(inflater)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEmailErrorText = mBinding.emailErrorText
        mEmailInputLayout = mBinding.emailInputLayout
        mEmailEdit = mBinding.emailEdit
        mPasswordEdit = mBinding.passwordEdit
        mPasswordErrorText = mBinding.passwordErrorText
        mPasswordInputLayout = mBinding.passwordInputLayout

        initListeners()
        subscribeToLiveData()
        mViewModel.isUserAuthorized()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.main_background_start)
        if (savedInstanceState != null) {
            mEmailEdit.setText(savedInstanceState.getString(EMAIL))
            mPasswordEdit.setText(savedInstanceState.getString(PASSWORD))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(EMAIL, mEmailEdit.text.toString())
        outState.putString(PASSWORD, mPasswordEdit.text.toString())
    }

    private fun subscribeToLiveData() {
        mViewModel.mWrongEmailLiveData.observe(this, Observer {
            it?.let {
                showErrorEmail(it)
            }
        })

        mViewModel.mUserAuthorized.observe(this, Observer {
            goToHomeFragment()
        })

        mViewModel.mPasswordErrorLiveData.observe(this, Observer {
            it?.let {
                showErrorPassword(it)
            }
        })

        mViewModel.mDatabaseErrorMessageLiveData.observe(this, Observer {
            it?.let {
                (activity as MainActivity).showErrorNotification(it)
            }
        })

        mViewModel.mLogInSuccessLiveData.observe(this, Observer {
            goToHomeFragment()
        })

        mViewModel.mLogInFailLiveData.observe(this, Observer {
            showErrorEmail(getString(R.string.log_in_error_text))
            showErrorPassword(getString(R.string.log_in_error_text))
        })

        mViewModel.mShowProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.VISIBLE
        })

        mViewModel.mHideProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.GONE
        })
    }

    private fun goToHomeFragment() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_signInFragment_to_homeFragment)
    }

    private fun initListeners() {

        mBinding.signInButton.setOnClickListener {
            mViewModel.signInUser(
                mEmailEdit.text.toString(),
                mPasswordEdit.text.toString()
            )
        }

        mBinding.signUpButton.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_signInFragment_to_signUpFragment)
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
    }

    private fun showErrorEmail(message: String) {
        mEmailErrorText.text = message
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

    private fun showErrorPassword(message: String) {
        mPasswordErrorText.text = message
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
}
