package com.cyl.musiclake.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import com.cyl.musiclake.event.AdapterEvent;
import com.cyl.musiclake.event.InfoChangeEvent;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.UIUtils;
import com.cyl.musiclake.ui.music.online.SpecailListAdapter;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;
import com.cyl.musiclake.ui.music.online.presenter.OnlinePlaylistPresenter;
import com.cyl.musiclake.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 磨耳朵电台专辑列表
 */
public class SpecialActivity extends BaseActivity<OnlinePlaylistPresenter> implements OnlinePlaylistContract.View {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R2.id.item_title_textview)
    TextView item_title_textview;
    private int position;//分类item点击监听

    private int itemPosition;//

    private List<SpecailRadioBean.DataBean> radios = new ArrayList<>();

    private SpecailListAdapter specailAdapter = null;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    @Override
    protected void initView() {
        item_title_textview.setVisibility(View.VISIBLE);
        item_title_textview.setText("专辑列表");
        mainContent.setBackgroundResource(R.color.white);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        specailAdapter = new SpecailListAdapter(radios);
        recyclerView.setAdapter(specailAdapter);
    }

    @Override
    protected void initData() {
        mPresenter.loadSpeData(true);
    }

    @Override
    protected void listener() {

    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void showErrorInfo(String msg) {

    }


    @Override
    public void showCharts(List<Playlist> charts) {

    }

    @Override
    public void showList(SpecailRadioBean data) {
        hideLoading();
        if (data.getData() != null) {
            radios = data.getData().getList();
            SpecailRadioBean.DataBean iData = new SpecailRadioBean.DataBean();
            List<SpecailRadioBean.DataBean.RadiosBean> radiosBeanList = new ArrayList<>();
            List<SpecailRadioBean.MyAudioDataBean> myAudios = data.getData().getMyAudios();
            if (myAudios != null && myAudios.size() > 0) {
                if (radios == null)
                    radios = new ArrayList<>();
                for (int i = 0; i < myAudios.size(); i++) {
                    SpecailRadioBean.MyAudioDataBean myAudioDataBean = myAudios.get(i);
                    SpecailRadioBean.DataBean.RadiosBean loadData = new SpecailRadioBean.DataBean.RadiosBean();
                    loadData.setRadioAudioCount(myAudioDataBean.getRadioAudios() == null ? 0 : myAudioDataBean.getRadioAudios().size());
                    loadData.setName(myAudioDataBean.getName());
                    loadData.setImg(myAudioDataBean.getTypeImg());
                    loadData.setId("shoucang"+i+loadData.getName());
                    if (myAudioDataBean.getRadioAudios() != null&&myAudioDataBean.getRadioAudios().size()>0) {
                        List<Music> mlIstanbul = new ArrayList<>();
                        for (int j = 0; j < myAudioDataBean.getRadioAudios().size(); j++) {
                            Music musicData = new Music();
                            QueryRadioAudioListBean.DataBean.RadioAudiosBean raData = myAudioDataBean.getRadioAudios().get(j);
                            musicData.setArtist(myAudioDataBean.getName());
                            musicData.setArtistId(loadData.getId());
                            musicData.setType(Constants.XIAOJIA);
                            musicData.setMid(raData.getId());
                            musicData.setTitle(raData.getName());
                            musicData.setUri(raData.getAudioUrl());
                            musicData.setCoverUri(myAudioDataBean.getTypeImg());
                            musicData.setDate(raData.getCreateTime());
                            musicData.setYear(raData.getPlayTime());
                            musicData.setTrackNumber(myAudioDataBean.getRadioAudios().size());
                            musicData.setLove(true);
                            if (SPUtils.getPlayPosition() == j && PlayManager.isPlaying() && SPUtils.getPId() != null && !TextUtils.isEmpty(SPUtils.getPId()) && SPUtils.getPId().equals(musicData.getArtistId())) {
                                musicData.setPlay(true);
                            } else {
                                musicData.setPlay(false);
                            }
                            ArrayList<QueryRadioAudioListBean.DataBean.RadioAudiosBean.LyricBaseInfoBeans> list = raData.getLyricJson().getList();
                            if (list != null) {
                                for (int k = 0; k < list.size(); k++) {
                                    QueryRadioAudioListBean.DataBean.RadioAudiosBean.LyricBaseInfoBeans da = list.get(k);
                                    da.setLrcTime(UIUtils.INSTANCE.hmTime(da.getTime()));
                                }
                                musicData.setLrcList(list);
                            }
                            StringBuffer lyric = new StringBuffer();
                            if (musicData.getLrcList() != null) {
                                for (int p = 0; p < musicData.getLrcList().size(); p++) {
                                    QueryRadioAudioListBean.DataBean.RadioAudiosBean.LyricBaseInfoBeans it = musicData.getLrcList().get(p);
                                    lyric.append("[" + it.getLrcTime() + ", " + it.getInfo().replace("\r", "") + "]");
                                    lyric.append("\n");
                                }
                                musicData.setLyric(lyric.toString());
                            }
                            mlIstanbul.add(musicData);
                        }
                        loadData.setMusicList(mlIstanbul);
                    }
                    if (loadData.getRadioAudioCount() > 0) {
                        radiosBeanList.add(loadData);
                    } else {
                        if (loadData.getName() != null && !TextUtils.isEmpty(loadData.getName()) && loadData.getName().equals("全部")) {
                            radiosBeanList.add(loadData);
                        }
                    }
                }
                iData.setName("我的收藏");
                if (radiosBeanList != null && radiosBeanList.size() > 0) {
                    iData.setRadios(radiosBeanList);
                    radios.add(0, iData);
                }
            }
            if (radios == null) return;
            for (int i = 0; i < radios.size(); i++) {
                List<SpecailRadioBean.DataBean.RadiosBean> raList = radios.get(i).getRadios();
                if (raList != null) {
                    for (int j = 0; j < raList.size(); j++) {
                        if (SPUtils.getPId() != null && !TextUtils.isEmpty(SPUtils.getPId()) && SPUtils.getPId().equals(raList.get(j).getId()) && PlayManager.isPlaying()) {
                            raList.get(j).setCheck(true);
                        } else {
                            raList.get(j).setCheck(false);
                        }
                    }
                }
            }
            specailAdapter.setNewData(radios);
        }
    }

    @Override
    public void showMusicList(QueryRadioAudioListBean data) {

    }

    @Override
    public void getDBMusicList(List<Music> muList) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMetaLinerdEvent(AdapterEvent event) {
        if (event == null) return;
        position = event.getPosition();
        itemPosition = event.getItemposition();
        if (event.getRadiosBean() == null) return;
        if (event.getRadiosBean().getRadioAudioCount() > 0) {
            NavigationHelper.INSTANCE.navigateToMusicPlaying(this,null,event.getRadiosBean());
        }
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void showError(String message, boolean showRetryButton) {
        super.showError(message, showRetryButton);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMetaChangedEvent(MetaChangedEvent event) {
        if (event != null && event.getMusic() == null) return;
        if (radios != null && radios.size() > 0) {
            for (int i = 0; i < radios.size(); i++) {
                List<SpecailRadioBean.DataBean.RadiosBean> radiosBeanlsit = radios.get(i).getRadios();
                    if (radiosBeanlsit != null) {
                        for (int j = 0; j < radiosBeanlsit.size(); j++) {
                            SpecailRadioBean.DataBean.RadiosBean radiosBean = radiosBeanlsit.get(j);
                            if (SPUtils.getPId() != null&& SPUtils.getPId().equals(radiosBean.getId())) {
                                if (PlayManager.isPlaying()) {
                                    radiosBean.setCheck(true);
                                } else {
                                    radiosBean.setCheck(false);
                                }
                            } else {
                                radiosBean.setCheck(false);
                            }
                        }
                    }
            }
            specailAdapter.setNewData(radios);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMeLoveState(InfoChangeEvent playlistEvent) {
        mPresenter.loadSpeData(true);
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
}
