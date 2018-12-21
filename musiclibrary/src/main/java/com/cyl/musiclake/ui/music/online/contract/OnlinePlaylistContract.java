package com.cyl.musiclake.ui.music.online.contract;


import android.app.Activity;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.bean.QueryRadioAudioListBean;
import com.cyl.musiclake.bean.SpecailRadioBean;

import java.util.List;

public interface OnlinePlaylistContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void showCharts(List<Playlist> charts);

        void showList(SpecailRadioBean data);

        void showMusicList(QueryRadioAudioListBean data);

        void getDBMusicList(List<Music> muList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void loadBaiDuPlaylist();

        void loadTopList();

        void loadSpeData(Boolean parentId, Activity activity);

        void loadMusicList(String aId, Activity activity);
    }
}
