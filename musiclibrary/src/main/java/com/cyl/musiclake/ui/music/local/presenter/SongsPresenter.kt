package com.cyl.musiclake.ui.music.local.presenter

import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.ui.music.local.contract.SongsContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by yonglong on 2018/1/7.
 */

class SongsPresenter @Inject
constructor() : BasePresenter<SongsContract.View>(), SongsContract.Presenter {

    override fun loadSongs(isReload: Boolean) {
        mView?.showLoading()
        doAsync {
            val data = SongLoader.getLocalMusic(mView.context, isReload)
            uiThread {
                mView?.showSongs(data)
            }
        }
    }
}
