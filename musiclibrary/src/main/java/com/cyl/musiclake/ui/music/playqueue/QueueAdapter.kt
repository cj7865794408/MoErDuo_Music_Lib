package com.cyl.musiclake.ui.music.playqueue

import android.graphics.Color
import android.support.v7.graphics.Palette
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.utils.ConvertUtils
import com.cyl.musiclake.utils.ImageUtils

/**
 * 功能：本地歌曲item
 * 作者：yonglong on 2016/8/8 19:44
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class QueueAdapter(musicList: List<Music>) : BaseQuickAdapter<Music, BaseViewHolder>(R.layout.item_queue, musicList) {

    private var mSwatch: Palette.Swatch? = null

    override fun convert(holder: BaseViewHolder, item: Music) {
        holder.setText(R.id.tv_title, ConvertUtils.getTitle(item.title))
        holder.setText(R.id.tv_artist, ConvertUtils.getArtistAndAlbum(item.artist, item.album))
        holder.getView<View>(R.id.item_line_id).visibility= View.VISIBLE
        holder.setImageResource(R.id.iv_resource, R.drawable.lottery_animlist)
        var animimo = ImageUtils.getAnimationDrawable(holder.getView<View>(R.id.iv_resource) as ImageView?)
        holder.getView<View>(R.id.iv_resource).visibility = View.VISIBLE
        if (PlayManager.getPlayingId() == item.mid) {
            if (animimo != null)
                animimo.start()
            holder.setTextColor(R.id.tv_title, Color.parseColor("#90c96b"))
            holder.setTextColor(R.id.tv_artist, Color.parseColor("#01579B"))
        } else {
            holder.getView<View>(R.id.iv_resource).visibility = View.GONE
            if (animimo != null)
                animimo.stop()
            holder.setTextColor(R.id.tv_title, Color.parseColor("#4e4e4e"))
            holder.setTextColor(R.id.tv_artist, Color.parseColor("#9e9e9e"))
        }
        holder.addOnClickListener(R.id.iv_more)
//        if (item.type == Constants.LOCAL) {
//            holder.getView<View>(R.id.iv_resource).visibility = View.GONE
//        } else {
//            holder.getView<View>(R.id.iv_resource).visibility = View.VISIBLE
//            when {
//                item.type == Constants.BAIDU -> {
//                    holder.setImageResource(R.id.iv_resource, R.drawable.baidu)
//                }
//                item.type == Constants.NETEASE -> {
//                    holder.setImageResource(R.id.iv_resource, R.drawable.netease)
//                }
//                item.type == Constants.QQ -> {
//                    holder.setImageResource(R.id.iv_resource, R.drawable.qq)
//                }
//                item.type == Constants.XIAMI -> {
//                    holder.setImageResource(R.id.iv_resource, R.drawable.xiami)
//                }
//            }
//        }
    }

}