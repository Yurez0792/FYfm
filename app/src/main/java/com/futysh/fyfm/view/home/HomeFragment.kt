package com.futysh.fyfm.view.home

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.futysh.fyfm.MainActivity
import com.futysh.fyfm.R
import com.futysh.fyfm.databinding.HomeFragmentLayoutBinding
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.model.room.User
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(), AlbumsAdapterListener {

    companion object {
        const val BASE_ALBUM_KEY = "base_album_key"
    }

    private val mViewModel by inject<HomeViewModel>()
    private var mUser: User? = null
    private lateinit var mProfileIconItem: MenuItem
    private lateinit var mBinding: HomeFragmentLayoutBinding

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

        subscribeToLiveData()
        mViewModel.getUserFromDB()
//        mViewModel.getTopAlbums(FM_TOP_ALBUMS_METHOD, FM_TOP_ARTISTS_TAG, FM_API_KEY, FM_FORMAT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).setSupportActionBar(mBinding.topAppBar)
        (activity as MainActivity).window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.dark_blue_color)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        mProfileIconItem = menu.findItem(R.id.profile_icon_item)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onAlbumItemClicked(album: BaseAlbum) {
        val bundle = Bundle()
        bundle.putParcelable(BASE_ALBUM_KEY, album)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_homeFragment_to_albumDetailFragment, bundle)
    }

    private fun subscribeToLiveData() {
        mViewModel.mProfileIconLiveData.observe(this, Observer {
            mProfileIconItem.actionView.findViewById<CircleImageView>(R.id.profile_image_view)
                .setImageBitmap(it)
        })

        mViewModel.mUserLiveData.observe(this, Observer {
            it?.let {
                mUser = it
//                mViewModel.getAvatar(mUser?.avatarPath, mUser?.userName)
//                mViewModel.getFavouritesAlbums(mUser?.userName)
            }
        })

        mViewModel.mShowProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.VISIBLE
        })

        mViewModel.mHideProgressLiveData.observe(this, Observer {
            mBinding.progressCircular.visibility = View.GONE
        })

        mViewModel.mGeneralErrorMessageLiveData.observe(this, Observer {
            (activity as MainActivity).showErrorNotification(it)
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
}