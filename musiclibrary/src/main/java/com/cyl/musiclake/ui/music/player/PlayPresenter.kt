package com.cyl.musiclake.ui.music.player

import android.annotation.SuppressLint
import android.support.v7.graphics.Palette
import android.text.TextUtils
import android.util.Log
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.QueryRadioAudioListBean
import com.cyl.musiclake.bean.SpecailRadioBean
import com.cyl.musiclake.data.PlaylistLoader
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.player.MusicPlayerService
import com.cyl.musiclake.player.playback.PlayProgressListener
import com.cyl.musiclake.utils.CoverLoader
import com.cyl.musiclake.utils.ImageUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


/**
 * Created by hefuyi on 2016/11/8.
 */

class PlayPresenter @Inject
constructor() : BasePresenter<PlayContract.View>(), PlayContract.Presenter, PlayProgressListener {
    override fun loadCollect(id: String?, state: Int?) {
        mView.showLoading()
        val observable = PlaylistApiServiceImpl.StudentAddAudioToFavorite(id!!, state!!)
        ApiManager.request(observable, object : RequestCallBack<SpecailRadioBean> {
            override fun success(result: SpecailRadioBean) {
                mView?.isCollect(result)
            }

            override fun error(msg: String) {
                mView.hideLoading()
            }
        })
    }

    override fun loadSpeData(getMyFavorite: Boolean?) {
        mView.showLoading()
        val observable = PlaylistApiServiceImpl.QueryRadioList(getMyFavorite!!)
        ApiManager.request(observable, object : RequestCallBack<SpecailRadioBean> {
            override fun success(result: SpecailRadioBean) {
                mView?.showList(result)
            }

            override fun error(msg: String) {
                mView.hideLoading()
            }
        })
    }

    override fun loadMusicList(aId: String) {
        mView?.showLoading()
        val observable = PlaylistApiServiceImpl.QueryRadioAudioList(aId!!)
        ApiManager.request(observable, object : RequestCallBack<QueryRadioAudioListBean> {
            override fun success(result: QueryRadioAudioListBean) {
                mView?.showMusicList(result)
            }

            override fun error(msg: String) {
                mView.hideLoading()
            }
        })
    }

    override fun onProgressUpdate(position: Long, duration: Long) {
        mView.updateProgress(position, duration)
    }

    override fun attachView(view: PlayContract.View) {
        super.attachView(view)
        MusicPlayerService.addProgressListener(this)
    }

    override fun detachView() {
        super.detachView()
        MusicPlayerService.removeProgressListener(this)
    }

    override fun updateNowPlaying(music: Music?) {
        mView?.showNowPlaying(music)
        CoverLoader.loadImageViewByMusic(mView?.context, music) { bitmap ->
            Palette.Builder(bitmap).generate { palette -> mView?.setPalette(palette) }
        }
        CoverLoader.loadBigImageView(mView?.context, music) { bitmap ->
            mView?.setPlayingBg(ImageUtils.createBlurredImageFromBitmap(bitmap, MusicApp.getAppContext(), 12))
            mView.setPlayingBitmap(bitmap)
        }
    }


    /**
     * 把音频列表插入或更新在数据库里
     */
    fun insertMusicListDB(sId: String?, list: List<Music>?) {
        doAsync {
            DaoLitepal.deleteAllMusic(sId!!)
            val data = sId?.let { PlaylistLoader.addMusicList(sId!!, list!!) }
            uiThread {
                if (data != null && !TextUtils.isEmpty(sId)) {
//                mView?.showPlaylistSongs(data)
                } else {
                    mView?.showEmptyState()
                }
            }
        }
    }

    fun createDB(sId: String?) {
        val data = sId?.let {
            @SuppressLint("LongLogTag")
            fun createDb(saId: String?) {
                var isCreatePid: Boolean = false
                try {
                    saId?.let {
                        var playlist: Playlist = PlaylistLoader.getPlaylist(saId!!)
                    }
                } catch (e: Throwable) {
                    isCreatePid = true
                }
                if (isCreatePid) {
                    try {
                        saId?.let {
                            PlaylistLoader.createXiaoJiaPlaylist(saId!!, "磨耳朵_$saId")
                        }
                    } catch (e: Throwable) {
                    }
                }
            }

        }
    }

    /**
     * 獲取數據庫裏對應專輯的音频列表
     */
    fun getMusicListDB(sId: String?, p: Int?, name: String?) {
        doAsync {
            createDB(sId)
            var data: List<Music> = DaoLitepal.getNewMusicList(sId!!)
            uiThread {
                if (data != null && !TextUtils.isEmpty(sId)) {
                    Log.e("getMusicListDB-----", data.size.toString())
                    mView?.getDBMusicList(data, p, name)
                } else {
                    mView?.showEmptyState()
                }
            }
        }
    }
}
