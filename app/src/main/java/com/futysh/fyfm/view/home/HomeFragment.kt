package com.futysh.fyfm.view.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.futysh.fyfm.databinding.HomeFragmentLayoutBinding

class HomeFragment : Fragment() {

    companion object {
        private const val AVATAR_KEY = "avatar"
    }

    private var mAvatar: Bitmap? = null
    private lateinit var mBinding: HomeFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAvatar = savedInstanceState?.getParcelable<Bitmap>(AVATAR_KEY)
        mBinding = HomeFragmentLayoutBinding.inflate(inflater)

        return mBinding.root
    }
}