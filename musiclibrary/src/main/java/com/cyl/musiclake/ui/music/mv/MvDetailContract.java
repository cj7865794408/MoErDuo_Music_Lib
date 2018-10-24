package com.cyl.musiclake.ui.music.mv;

import com.cyl.musicapi.netease.CommentsItemInfo;
import com.cyl.musicapi.netease.MvInfoDetail;
import com.cyl.musicapi.netease.MvInfoDetailInfo;
import com.cyl.musiclake.base.BaseContract;

import java.util.List;


public interface MvDetailContract {

    interface View extends BaseContract.BaseView {
        void showMvList(List<MvInfoDetail> mvList);

        void showMvDetailInfo(MvInfoDetailInfo mvInfoDetailInfo);

        void showMvHotComment(List<CommentsItemInfo> mvHotCommentInfo);

        void showMvComment(List<CommentsItemInfo> mvCommentInfo);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadMv(int offset);

        void loadMvDetail(String mvid);

        void loadSimilarMv(String mvid);

        void loadMvComment(String mvid);
    }
}
