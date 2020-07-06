package com.futysh.fyfm.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.futysh.fyfm.R
import com.futysh.fyfm.databinding.AlbumItemLayoutBinding
import com.futysh.fyfm.model.room.BaseAlbum
import com.futysh.fyfm.utils.Constants.Companion.ROUNDED_CORNERS_RADIUS
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class FavoriteAlbumsAdapter(
    private val topAlbums: List<BaseAlbum>,
    private val albumsAdapterListener: AlbumsAdapterListener
) :
    RecyclerView.Adapter<FavoriteAlbumsAdapter.AlbumViewHolder>() {

    private lateinit var mBinding: AlbumItemLayoutBinding
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        mContext = parent.context
        mBinding = AlbumItemLayoutBinding.inflate(LayoutInflater.from(mContext))

        return AlbumViewHolder(mBinding, topAlbums)
    }

    override fun getItemCount(): Int {
        return topAlbums.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = topAlbums[position]
        holder.albumTitle.text = album.albumName
        setAlbumImage(
            holder.albumImage,
            album.imageUrl
        )

    }

    private fun setAlbumImage(albumImageView: ImageView, url: String?) {
        Picasso.Builder(mContext).build()
            .load(url)
            .error(R.drawable.ic_launcher_background)
            .transform(RoundedCornersTransformation(ROUNDED_CORNERS_RADIUS, ROUNDED_CORNERS_RADIUS))
            .into(albumImageView)
    }

    inner class AlbumViewHolder(
        artistItemLayoutBinding: AlbumItemLayoutBinding,
        private val favouriteAlbums: List<BaseAlbum>
    ) :
        RecyclerView.ViewHolder(artistItemLayoutBinding.root) {

        val albumImage = artistItemLayoutBinding.albumImage
        val albumTitle = artistItemLayoutBinding.albumTitleText

        init {
            artistItemLayoutBinding.root.setOnClickListener {
                albumsAdapterListener.onAlbumItemClicked(favouriteAlbums[adapterPosition])
            }
        }
    }
}