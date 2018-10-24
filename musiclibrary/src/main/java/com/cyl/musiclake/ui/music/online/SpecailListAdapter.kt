package com.cyl.musiclake.ui.music.online

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cyl.musiclake.R
import com.cyl.musiclake.bean.SpecailRadioBean
import com.cyl.musiclake.event.AdapterEvent
import org.greenrobot.eventbus.EventBus

class SpecailListAdapter(playlist: List<SpecailRadioBean.DataBean>) : BaseQuickAdapter<SpecailRadioBean.DataBean, BaseViewHolder>(R.layout.specail_list_view, playlist) {

    override fun convert(helper: BaseViewHolder, playlist: SpecailRadioBean.DataBean) {
        var mAdapter: SpecailAdapter? = null
        var list_recyview = helper.getView<RecyclerView>(R.id.list_recyview)
        if (playlist.radios != null && playlist.radios.size > 0) {
            helper.setGone(R.id.type_text_id, true)
            helper.setText(R.id.type_text_id, playlist.name)
            mAdapter = SpecailAdapter(playlist.radios)
            var layoutManager = GridLayoutManager(mContext, 3)
            list_recyview?.layoutManager = layoutManager
            list_recyview?.adapter = mAdapter
            mAdapter?.bindToRecyclerView(list_recyview)
            val aPposition = helper.adapterPosition

            mAdapter.setOnItemClickListener { _, _, position ->
                EventBus.getDefault().post(AdapterEvent(aPposition, position, playlist.radios[position]))
            }
        } else {
            helper.setGone(R.id.type_text_id, false)
        }
    }
}