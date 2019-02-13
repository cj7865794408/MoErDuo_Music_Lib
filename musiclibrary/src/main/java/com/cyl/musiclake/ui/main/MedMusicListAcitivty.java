package com.cyl.musiclake.ui.main;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.R2;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.bean.QueryRadioAudioListBean;
import com.cyl.musiclake.bean.SpecailRadioBean;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlayMusicEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.music.online.MedMusicAdapter;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;
import com.cyl.musiclake.ui.music.online.presenter.OnlinePlaylistPresenter;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;

public class MedMusicListAcitivty extends BaseActivity<OnlinePlaylistPresenter> implements OnlinePlaylistContract.View {

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.error_message_view)
    TextView errorMessageView;
    @BindView(R2.id.error_button_retry)
    Button errorButtonRetry;
    @BindView(R2.id.spe_img_id)
    ImageView speImgId;
    @BindView(R2.id.spe_music_name)
    TextView speMusicName;
    @BindView(R2.id.spe_music_des)
    TextView speMusicDes;
    @BindView(R2.id.spe_play_id)
    ImageView spePlayId;
    @BindView(R2.id.spe_card_id)
    CardView speCardId;
    @BindView(R2.id.item_title_textview)
    TextView item_title_textview;
    private MedMusicAdapter medMusicAdapter = null;
    private SpecailRadioBean.DataBean.RadiosBean radiosData;
    private List<QueryRadioAudioListBean.DataBean.RadioAudiosBean> radioAudios = new ArrayList<>();

    private Playlist playlist;
    private ArrayList<Music> musicList = new ArrayList<>();
    private ArrayList<Music> currtMusicList = new ArrayList<>();
    private String itemMid = "";

    private Music currtMusicData = null;
    ObjectAnimator coverAnimator = null;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_newgc_notoolbar;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        radiosData = Constants.radiosData;
        if (radiosData != null) {
            item_title_textview.setVisibility(View.VISIBLE);
            item_title_textview.setText(radiosData.getName() + "");
            playlist = new Playlist();
            playlist.setType(1);
            playlist.setPid(radiosData.getId());
            playlist.setName(radiosData.getName());
            playlist.setCoverUrl(radiosData.getImg());
            playlist.setCount(radiosData.getRadioAudioCount());
            recyclerView.setLayoutManager(new LinearLayoutManager(this) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            medMusicAdapter = new MedMusicAdapter(musicList, playlist);
            recyclerView.setAdapter(medMusicAdapter);
            if (radiosData.getMusicList() != null && radiosData.getMusicList().size() > 0) {
                musicList = (ArrayList<Music>) radiosData.getMusicList();
                playlist.setMusicList(musicList);
                medMusicAdapter.setNewData(musicList);
                loadHeader();
            } else {
                mPresenter.loadMusicList(radiosData.getId(), this);
            }
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    public void loadHeader() {
        mPresenter.getMusicListDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (coverAnimator != null) {
            if (coverAnimator.isStarted())
                coverAnimator.resume();
            else
                coverAnimator.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (coverAnimator != null) {
            coverAnimator.pause();
        }
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void listener() {
        medMusicAdapter.setOnItemClickListener((adapter, view, position) -> {
            boolean isstart = false;
            for (int i = 0; i < musicList.size(); i++) {
                if (i == position) {
                    musicList.get(i).setPlay(!musicList.get(i).isPlay());
                    isstart = musicList.get(i).isPlay();
                } else {
                    musicList.get(i).setPlay(false);
                }
            }
            SPUtils.setPid(playlist.getPid());
            mPresenter.insertMusicListDB(playlist.getPid(), musicList);
            if (!isstart) {
                if (musicList != null)
                    NavigationHelper.INSTANCE.navigateToPlaying(this, view.findViewById(R.id.iv_cover), position, musicList, musicList.get(position).getArtist() + musicList.get(position).getArtistId());
            } else {
                EventBus.getDefault().post(new PlayMusicEvent(position, musicList.get(position).getArtist() + musicList.get(position).getArtistId(), musicList));
            }
            playlist.setMusicList(musicList);
            medMusicAdapter.setNewData(musicList);
        });
    }

    @Override
    public void showErrorInfo(String msg) {

    }

    @Override
    public void showCharts(List<Playlist> charts) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMetaChangedEvent(MetaChangedEvent event) {
        if (event != null && event.getMusic() == null) return;
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getMid().equals(event.getMusic().getMid())) {
                if (PlayManager.isPlaying()) {
                    musicList.get(i).setPlay(true);
                } else {
                    musicList.get(i).setPlay(false);
                }
            } else {
                musicList.get(i).setPlay(false);
            }
        }
        playlist.setMusicList(musicList);
        medMusicAdapter.setNewData(musicList);
        loadHeader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void showList(SpecailRadioBean data) {

    }

    @Override
    public void showMusicList(QueryRadioAudioListBean data) {
        hideLoading();
        if (data.getData().getRadioAudios() == null) return;
        String midId = PlayManager.getPlayingId();
        Log.e("data=", data.getData().getRadioAudios().size() + "");
        musicList = new ArrayList<>();
        int num = data.getData().getRadioAudios().size();
        for (int k = 0; k < data.getData().getRadioAudios().size(); k++) {
            QueryRadioAudioListBean.DataBean.RadioAudiosBean raData = data.getData().getRadioAudios().get(k);
            Music newData = new Music();
            if (raData.getId().equals(midId) && PlayManager.isPlaying() && SPUtils.getPId() != null && !TextUtils.isEmpty(SPUtils.getPId()) && SPUtils.getPId().equals(playlist.getPid())) {
                newData.setPlay(true);
            } else {
                newData.setPlay(false);
            }
            newData.setArtist(playlist.getName());
            newData.setArtistId(playlist.getPid());
            newData.setType(Constants.XIAOJIA);
            newData.setMid(raData.getId());
            newData.setTitle(raData.getName());
            newData.setUri(raData.getAudioUrl());
            newData.setTrackNumber(num);
            newData.setCoverUri(playlist.getCoverUrl());
            newData.setDate(raData.getCreateTime());
            newData.setYear(raData.getPlayTime());
            newData.setLove(raData.isHasFavorite());
            ArrayList<QueryRadioAudioListBean.DataBean.RadioAudiosBean.LyricBaseInfoBeans> list = new ArrayList<>();
            if (raData.getLyricJson() != null) {
                list = raData.getLyricJson().getList();
            }
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    QueryRadioAudioListBean.DataBean.RadioAudiosBean.LyricBaseInfoBeans da = list.get(i);
                    da.setLrcTime(hmTime(da.getTime()));
                }
                newData.setLrcList(list);
            }
            StringBuffer lyric = new StringBuffer();
            if (newData.getLrcList() != null) {
                for (int i = 0; i < newData.getLrcList().size(); i++) {
                    QueryRadioAudioListBean.DataBean.RadioAudiosBean.LyricBaseInfoBeans it = newData.getLrcList().get(i);
                    lyric.append("[" + it.getLrcTime() + ", " + it.getInfo().replace("\r", "") + "]");
                    lyric.append("\n");
                }
                newData.setLyric(lyric.toString());
            }
            musicList.add(newData);
        }
        playlist.setMusicList(musicList);
        medMusicAdapter.setNewData(musicList);
        loadHeader();
    }

    @Override
    public void getDBMusicList(List<Music> muList) {
        currtMusicData = PlayManager.getPlayingMusic();
        if (currtMusicData == null) return;
        currtMusicList = musicList;
        speCardId.setVisibility(View.VISIBLE);
        speMusicName.setText(currtMusicData.getTitle() + "");
        speMusicDes.setText("当前播放专辑 <<" + currtMusicData.getArtist() + ">>");
        if (PlayManager.isPlaying()) {
            spePlayId.setBackgroundResource(R.drawable.green_pause_icon);
        } else {
            spePlayId.setBackgroundResource(R.drawable.green_play_icon);
        }
        speCardId.setOnClickListener(view -> NavigationHelper.INSTANCE.navigateToPlaying(MedMusicListAcitivty.this, view.findViewById(R.id.iv_cover), SPUtils.getPlayPosition(), currtMusicList, currtMusicData.getArtist() + currtMusicData.getArtistId()));
        spePlayId.setOnClickListener(view -> {
            PlayManager.playPause();
            if (PlayManager.isPlaying()) {
                spePlayId.setBackgroundResource(R.drawable.green_pause_icon);
            } else {
                spePlayId.setBackgroundResource(R.drawable.green_play_icon);
            }
        });
        CoverLoader.loadImageView(this, currtMusicData.getCoverUri(), speImgId);
        if (coverAnimator == null) {
            coverAnimator = ObjectAnimator.ofFloat(speImgId, "rotation", 0F, 359F);
            coverAnimator.setDuration(20 * 1000);
            coverAnimator.setRepeatCount(-1);
            coverAnimator.setRepeatMode(ObjectAnimator.RESTART);
            if (coverAnimator != null)
                coverAnimator.start();
        }
    }

    public String hmTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String fmTime = formatter.format(time);
        return fmTime + ":00";
    }

}
