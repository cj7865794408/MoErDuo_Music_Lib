package com.cyl.musiclake.ui.music.playqueue

import android.text.TextUtils
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.utils.SPUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by yonglong on 2018/1/7.
 */

class PlayQueuePresenter @Inject
constructor() : BasePresenter<PlayQueueContract.View>(), PlayQueueContract.Presenter {

    override fun attachView(view: PlayQueueContract.View) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
    }

    override fun loadSongs() {
        if (SPUtils.getPId() == null) {
            mView.showEmptyView()
            return
        }
        doAsync {
            var data: List<Music> = DaoLitepal.getNewMusicList(SPUtils.getPId()!!)
            uiThread {
                if (data != null && data.isNotEmpty() && !TextUtils.isEmpty(SPUtils.getPId())) {
                    mView.showSongs(data)
                } else {
                    mView.showEmptyView()
                }
            }
        }
    }

    override fun clearQueue() {
        PlayManager.clearQueue()
        mView.showEmptyView()
    }
}
