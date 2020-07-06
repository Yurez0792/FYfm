package com.futysh.fyfm.view.home

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.futysh.fyfm.MainActivity
import com.futysh.fyfm.R
import com.futysh.fyfm.databinding.HomeFragmentLayoutBinding
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.model.room.User
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.ext.android.inject


class HomeFragment : Fragment(), AlbumsAdapterListener {

    companion object {
        const val BASE_ALBUM_KEY = "base_album_key"
    }

    private val mViewModel by inject<HomeViewModel>()
    private var mUser: User? = null
    private lateinit var mProfileMenuItem: MenuItem
    private lateinit var mBinding: HomeFragmentLayoutBinding
    private lateinit var mToolbar: MaterialToolbar
    private lateinit var mNavHostFragment: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = HomeFragmentLayoutBinding.inflate(inflater)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefreshLayout = mBinding.swipeRefreshLayout
        mToolbar = mBinding.toolbar
        swipeRefreshLayout.setOnRefreshListener {
            getUser()
            swipeRefreshLayout.isRefreshing = false
        }

        subscribeToLiveData()
        getUser()
    }

    private fun getUser() {
        if (isInternetAvailable()) {
            mViewModel.getUserFromDB()
        }
    }

    private fun isInternetAvailable(): Boolean {
        return if ((activity as MainActivity).isInternetAvailable()) {
            true
        } else {
            showError(getString(R.string.turn_on_internet_connection_text))
            false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).setSupportActionBar(mToolbar)
        (activity as MainActivity).window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.dark_blue_color)

        mNavHostFragment = NavHostFragment.findNavController(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        mProfileMenuItem = menu.findItem(R.id.profile_menu_item)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_out_item -> {
                mViewModel.logOut()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onAlbumItemClicked(album: BaseAlbum) {
        val bundle = Bundle()
        bundle.putParcelable(BASE_ALBUM_KEY, album)
        mNavHostFragment.navigate(R.id.action_homeFragment_to_albumDetailFragment, bundle)
    }

    private fun subscribeToLiveData() {
        mViewModel.mProfileIconLiveData.observe(this, Observer {
            mProfileMenuItem.icon = it
        })

        mViewModel.mUserLiveData.observe(this, Observer {
            it?.let {
                mUser = it
            }
        })

        mViewModel.mShowProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.VISIBLE
        })

        mViewModel.mHideProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.GONE
        })

        mViewModel.mIsLoggedOut.observe(this, Observer {
            mNavHostFragment.navigate(R.id.action_homeFragment_to_signInFragment)
        })

        mViewModel.mGeneralErrorMessageLiveData.observe(this, Observer {
            showError(it)
        })

        mViewModel.mAlbumsLiveData.observe(this, Observer {
            it?.let {
                mBinding.recommendedTitleText.visibility = View.VISIBLE
                val topAlbumsRecycler = mBinding.topAlbumsRecycler
                topAlbumsRecycler.visibility = View.VISIBLE
                topAlbumsRecycler.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                topAlbumsRecycler.adapter = AlbumsAdapter(it, this, mUser!!.userName)
            }
        })

        mViewModel.mFavouritesAlbumsLiveData.observe(this, Observer {
            mBinding.favouritesTitle.visibility = View.VISIBLE
            val favouritesRecycler = mBinding.favouritesRecycler
            favouritesRecycler.visibility = View.VISIBLE
            favouritesRecycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            favouritesRecycler.adapter = FavoriteAlbumsAdapter(it, this)
        })
    }

    private fun showError(errorMessage: String) {
        (activity as MainActivity).showErrorNotification(errorMessage)
    }
}