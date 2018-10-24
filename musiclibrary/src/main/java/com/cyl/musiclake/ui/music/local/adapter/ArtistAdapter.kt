package com.cyl.musiclake.ui.music.local.adapter

import android.app.Activity
import android.os.Build
import android.util.Pair
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicApi
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.common.NavigationHelper
import com.cyl.musiclake.utils.CoverLoader

class ArtistAdapter(private val artistList: List<Artist>) : BaseQuickAdapter<Artist, BaseViewHolder>(R.layout.item_playlist_grid, artistList) {

    override fun convert(helper: BaseViewHolder, artist: Artist) {
        helper.setText(R.id.name, artist.name)
        helper.setText(R.id.artist, artist.musicSize.toString() + "首歌")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView<ImageView>(R.id.album).transitionName = Constants.TRANSTITION_ALBUM
        }
        CoverLoader.loadImageView(mContext, artist.picUrl, helper.getView(R.id.album))
        if (artist.picUrl.isNullOrEmpty()) {
            artist.name?.let {
                MusicApi.getMusicAlbumPic(artist.name.toString(), success = {
                    artist.picUrl = it
                    artist.save()
                    CoverLoader.loadImageView(mContext, it, helper.getView(R.id.album))
                })
            }
        }
        helper.itemView.setOnClickListener {
            NavigationHelper.navigateToPlaylist(mContext as Activity, artist, Pair(helper.getView(R.id.album), Constants.TRANSTITION_ALBUM))
        }
    }
}