package com.cyl.musiclake.ui.music.discover

import com.cyl.musicapi.netease.MvComment
import com.cyl.musicapi.netease.MvDetailInfo
import com.cyl.musicapi.netease.MvInfo
import com.cyl.musicapi.netease.SimilarMvInfo
import com.cyl.musiclake.base.BasePresenter
import com.cyl.musiclake.net.RequestCallBack
import com.cyl.musiclake.ui.music.mv.MvDetailContract
import com.cyl.musiclake.ui.music.mv.MvModel
import javax.inject.Inject

/**
 * Des    :
 * Author : master.
 * Date   : 2018/5/20 .
 */
class MvDetailPresenter @Inject
constructor() : BasePresenter<MvDetailContract.View>(), MvDetailContract.Presenter {
    private val mvModel = MvModel()
    override fun loadMvDetail(mvid: String?) {
        mvModel.loadMvDetail(mvid, object : RequestCallBack<MvDetailInfo> {
            override fun success(result: MvDetailInfo?) {
                mView?.hideLoading()
                result?.data?.let {
                    mView?.showMvDetailInfo(it)
                }
            }

            override fun error(msg: String?) {
                mView?.hideLoading()
                mView?.showError(msg, true)
            }

        })
    }

    override fun loadMv(offset: Int) {
        mvModel.loadMv(offset, object : RequestCallBack<MvInfo> {
            override fun success(result: MvInfo?) {
                result?.data?.let {
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
            }

        })
    }

    override fun loadSimilarMv(mvid: String?) {
        mvModel.loadSimilarMv(mvid, object : RequestCallBack<SimilarMvInfo> {
            override fun success(result: SimilarMvInfo?) {
                result?.data?.let {
                    mView?.showMvList(it)
                }
            }

            override fun error(msg: String?) {
            }

        })
    }

    override fun loadMvComment(mvid: String?) {
        mvModel.loadMvComment(mvid, object : RequestCallBack<MvComment> {
            override fun success(result: MvComment?) {
                result?.comments?.let {
                    mView?.showMvComment(it)
                }
                result?.hotComments?.let {
                    mView?.showMvHotComment(it)
                }
            }

            override fun error(msg: String?) {
            }

        })
    }
}
