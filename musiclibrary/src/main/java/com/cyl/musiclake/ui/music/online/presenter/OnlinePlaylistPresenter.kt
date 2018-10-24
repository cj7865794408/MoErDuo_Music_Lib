package com.cyl.musiclake.ui.music.online.presenter

import android.text.TextUtils
import android.util.Log
import com.cyl.musiclake.api.PlaylistApiServiceImpl
import com.cyl.musiclake.api.baidu.BaiduApiServiceImpl
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.QueryRadioAudioListBean
import com.cyl.musiclake.bean.SpecailRadioBean
import com.cyl.musiclake.data.PlaylistLoader
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract
import com.cyl.musiclake.utils.SPUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

/**
 * Created by D22434 on 2018/1/4.
 */

class OnlinePlaylistPresenter @Inject
constructor() : BasePresenter<OnlinePlaylistContract.View>(), OnlinePlaylistContract.Presenter {

    override fun loadMusicList(aId: String?) {
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

    override fun loadBaiDuPlaylist() {
        BaiduApiServiceImpl.getOnlinePlaylist()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(object : Observer<List<Playlist>> {
                    override fun onSubscribe(d: Disposable) {
                        mView.showLoading()
                    }

                    override fun onNext(result: List<Playlist>) {
                        mView.showCharts(result)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView.hideLoading()
                        mView.showErrorInfo(e.message)
                    }

                    override fun onComplete() {
                        mView.hideLoading()
                    }
                })
    }

    override fun loadTopList() {
        val observable = PlaylistApiServiceImpl.getNeteaseRank(IntArray(22) { i -> i }, 3)
        ApiManager.request(observable, object : RequestCallBack<MutableList<Playlist>> {
            override fun success(result: MutableList<Playlist>) {
                mView?.showCharts(result)
            }

            override fun error(msg: String) {
                mView.hideLoading()
            }
        })
    }

    override fun loadSpeData(getMyFavorite: Boolean) {
        mView.showLoading()
        val observable = PlaylistApiServiceImpl.QueryRadioList(getMyFavorite)
        ApiManager.request(observable, object : RequestCallBack<SpecailRadioBean> {
            override fun success(result: SpecailRadioBean) {
                mView?.showList(result)
            }

            override fun error(msg: String) {
                mView.hideLoading()
            }
        })
    }

    /**
     * 獲取數據庫裏對應專輯的音频列表
     */
    fun getMusicListDB() {
        doAsync {
            var data: List<Music> = DaoLitepal.getNewMusicList(SPUtils.getPId())
            uiThread {
                if (data != null && !TextUtils.isEmpty(SPUtils.getPId())) {
                    Log.e("getMusicListDB-----", data.size.toString())
                    mView?.getDBMusicList(data)
                } else {
                    mView?.showEmptyState()
                }
            }
        }
    }
    /**
     * 把音频列表插入或更新在数据库里
     */
    fun insertMusicListDB(sId: String?, list: List<Music>?) {
        doAsync {
            val data = sId?.let {
                DaoLitepal.deleteAllMusic(sId!!)
                PlaylistLoader.addMusicList(sId!!, list!!)
            }
            uiThread {
                if (data != null && !TextUtils.isEmpty(sId)) {
//                mView?.showPlaylistSongs(data)
                } else {
                    mView?.showEmptyState()
                }
            }
        }
    }
}
