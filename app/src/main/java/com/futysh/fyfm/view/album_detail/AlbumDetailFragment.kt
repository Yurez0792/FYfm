package com.futysh.fyfm.view.album_detail

import android.os.Bundle
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
import com.futysh.fyfm.databinding.AlbumDetailFragmentLayoutBinding
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.utils.Constants.Companion.ROUNDED_CORNERS_RADIUS
import com.futysh.fyfm.view.home.HomeFragment.Companion.BASE_ALBUM_KEY
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import org.koin.android.ext.android.inject

class AlbumDetailFragment : Fragment() {

    private val mViewModel by inject<AlbumDetailViewModel>()
    private lateinit var mAlbum: BaseAlbum
    private lateinit var mHeartImage: ImageView
    private lateinit var mAlbumTitleText: TextView
    private lateinit var mBinder: AlbumDetailFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAlbum = arguments?.getParcelable<BaseAlbum>(BASE_ALBUM_KEY)!!
        mBinder =
            AlbumDetailFragmentLayoutBinding.inflate(LayoutInflater.from(context), container, false)

        return mBinder.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.mDatabaseErrorMessageLiveData.observe(this, Observer {
            (activity as MainActivity).showErrorNotification(it)
        })

        mViewModel.mFavouriteAlbumLiveData.observe(this, Observer {
            it?.let {
                mAlbum = it
            }
            fillWidgets()
            setHeartImage(mAlbum.isSelected)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAlbumTitleText = mBinder.albumTitleText
        mHeartImage = mBinder.heartImage

        initListeners()

        mViewModel.getFavouriteFromDB(mAlbum)
    }

    private fun fillWidgets() {
        mBinder.albumTitleText.text = mAlbum.albumName
        mBinder.albumRateText.text = getString(R.string.rank_text, mAlbum.rank?.toString())
        mAlbum.imageUrl?.let { setAlbumImage(mBinder.albumImage, it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAlbumTitleText.width = (activity as MainActivity).getScreenWidth() / 2
    }

    private fun setHeartImage(selected: Boolean) {
        if (selected) {
            mHeartImage.setImageDrawable(resources.getDrawable(R.drawable.ic_heart_selected, null))
        } else {
            mHeartImage.setImageDrawable(resources.getDrawable(R.drawable.ic_heart, null))
        }
    }

    private fun initListeners() {
        mBinder.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_albumDetailFragment_to_homeFragment)
        }

        mBinder.heartImage.setOnClickListener {
            val selected = mAlbum.isSelected
            mAlbum.isSelected = !selected
            processByDatabase(mAlbum)
            setHeartImage(!selected)
        }
    }

    private fun processByDatabase(album: BaseAlbum) {
        mViewModel.processByDatabase(album)
    }

    private fun setAlbumImage(albumImageView: ImageView, url: String) {
        Picasso.Builder(requireContext()).build()
            .load(url)
            .error(R.drawable.ic_launcher_background)
            .transform(RoundedCornersTransformation(ROUNDED_CORNERS_RADIUS, ROUNDED_CORNERS_RADIUS))
            .into(albumImageView)
    }
}