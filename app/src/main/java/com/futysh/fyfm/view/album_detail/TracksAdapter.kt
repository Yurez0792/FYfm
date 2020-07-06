package com.futysh.fyfm.view.album_detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.futysh.fyfm.R
import com.futysh.fyfm.databinding.TrackItemLayoutBinding
import com.futysh.fyfm.model.last_fm.artist_top_albums.Album
import com.futysh.fyfm.utils.Constants
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class TracksAdapter(
    private val topTracks: List<Album>
) :
    RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {
    companion object {
        private const val MOCK_IMAGE_URL =
            "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
    }

    private lateinit var mBinding: TrackItemLayoutBinding
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        mContext = parent.context
        mBinding = TrackItemLayoutBinding.inflate(LayoutInflater.from(mContext))

        return TrackViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return topTracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = topTracks[position]
        holder.trackTitle.text = track.name
        setAlbumImage(holder.trackImage, MOCK_IMAGE_URL)
    }

    private fun setAlbumImage(albumImageView: ImageView, url: String) {
        Picasso.Builder(mContext).build()
            .load(url)
            .error(R.drawable.ic_launcher_background)
            .transform(
                RoundedCornersTransformation(
                    Constants.ROUNDED_CORNERS_RADIUS,
                    Constants.ROUNDED_CORNERS_RADIUS
                )
            )
            .into(albumImageView)
    }

    inner class TrackViewHolder(
        artistItemLayoutBinding: TrackItemLayoutBinding
    ) :
        RecyclerView.ViewHolder(artistItemLayoutBinding.root) {

        val trackImage = artistItemLayoutBinding.trackImage
        val trackTitle = artistItemLayoutBinding.trackTitle
    }
}