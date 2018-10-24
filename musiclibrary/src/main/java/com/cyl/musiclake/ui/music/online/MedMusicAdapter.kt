package com.cyl.musiclake.ui.music.online

import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.utils.ImageUtils

/**
 * 版本：2.5
 */
class MedMusicAdapter(playlist: List<Music>, val dataList: Playlist) : BaseQuickAdapter<Music, BaseViewHolder>(R.layout.med_music_item_view, playlist) {
    override fun convert(helper: BaseViewHolder, playlist: Music) {
        var imagiew: ImageView? = helper.getView(R.id.spe_animi_id)
        imagiew?.setImageResource(R.drawable.lottery_animlist)
        var animimo = ImageUtils.getAnimationDrawable(imagiew)
        if (playlist.isPlay) {
            helper.setTextColor(R.id.title, ContextCompat.getColor(mContext, R.color.lightGreen))
            helper.setGone(R.id.spe_animi_id, true)
            if (animimo != null)
                animimo.start()
        } else {
            helper.setGone(R.id.spe_animi_id, false)
            helper.setTextColor(R.id.title, ContextCompat.getColor(mContext, R.color.black2))
            if (animimo != null && animimo.isRunning)
                animimo.stop()
        }
        helper.setText(R.id.title, playlist.title)
//        for (i in 0 until dataList) {
//            if (playlist.musicList.size <= i) continue
//            val music = playlist.musicList[i]
//            helper.setText(viewIds[i], mContext.getString(stringIds[i],
//                    music.title, music.artist))
//        }
    }
}