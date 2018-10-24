package com.cyl.musiclake.ui.music.online

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.SpecailRadioBean
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ImageUtils

/**
 * 作者：cj on 2016/8/10 21:36
 * 版本：2.5
 */
class SpecailAdapter(playlist: List<SpecailRadioBean.DataBean.RadiosBean>) : BaseQuickAdapter<SpecailRadioBean.DataBean.RadiosBean, BaseViewHolder>(R.layout.item_online_specail, playlist) {
    override fun convert(helper: BaseViewHolder, playlist: SpecailRadioBean.DataBean.RadiosBean) {
                    helper.setGone(R.id.zhuangxiang_id, false)
                    if (playlist.id.equals("love")) {
                        helper.setGone(R.id.zhuangxiang_id, true)
                        helper.setBackgroundRes(R.id.iv_cover, R.drawable.book_bg_img_icon)
                    }else
                        helper.setBackgroundRes(R.id.iv_cover, 0)

                    var imagiew: ImageView? = helper.getView(R.id.iv_cover_play)
                        imagiew?.setImageResource(R.drawable.spe_lottery_animlist)
                    var animimo = ImageUtils.getAnimationDrawable(imagiew)
                    if (playlist.isCheck) {

                        if (animimo != null)
                            animimo.start()
                    } else {
                        imagiew?.setImageResource(R.mipmap.radis_play_tm_icon)
                        if (animimo != null)
                            animimo.stop()
        }
        CoverLoader.loadZJImageView(mContext, playlist.img, helper.getView(R.id.iv_cover))
        helper.setText(R.id.title, playlist.name)
        helper.setText(R.id.title_num, "-" + playlist.radioAudioCount + "首-")
    }
}