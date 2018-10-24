package com.cyl.musiclake.download.ui


import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.download.TasksManagerModel

interface DownloadContract {

    interface View : BaseContract.BaseView {
        fun showErrorInfo(msg: String)

        fun showSongs(musicList: List<Music>)

        fun showDownloadList(modelList: List<TasksManagerModel>)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadDownloadMusic()

        fun loadDownloading()
    }
}
