package com.cyl.musiclake.ui.music.online.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.R2;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.bean.Playlist;
import com.cyl.musiclake.bean.QueryRadioAudioListBean;
import com.cyl.musiclake.bean.SpecailRadioBean;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.music.online.OnlineAdapter;
import com.cyl.musiclake.ui.music.online.activity.BaiduMusicListActivity;
import com.cyl.musiclake.ui.music.online.contract.OnlinePlaylistContract;
import com.cyl.musiclake.ui.music.online.presenter.OnlinePlaylistPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：在线排行榜
 * 作者：yonglong on 2016/8/11 18:14
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class BaiduPlaylistFragment extends BaseFragment<OnlinePlaylistPresenter> implements OnlinePlaylistContract.View {

    private static final String TAG = "BaiduPlaylistFragment";
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    //适配器
    private OnlineAdapter mAdapter;
    private List<Playlist> playlist = new ArrayList<>();

    public static BaiduPlaylistFragment newInstance() {
        Bundle args = new Bundle();
        BaiduPlaylistFragment fragment = new BaiduPlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getToolBarTitle() {
        return getString(R.string.charts_baidu);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void initViews() {
        //初始化列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //适配器
        mAdapter = new OnlineAdapter(playlist);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void loadData() {
//        初始化列表,当无数据时显示提示
        mPresenter.loadBaiDuPlaylist();
    }

    @Override
    protected void listener() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mPresenter.loadBaiDuPlaylist();
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Playlist playlist = (Playlist) adapter.getItem(position);
            Intent intent = new Intent(getActivity(), BaiduMusicListActivity.class);
            intent.putExtra(Extras.PLAYLIST, playlist);
            startActivity(intent);
        });
    }

    @Override
    public void showLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorInfo(String msg) {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }

    @Override
    public void showCharts(List<Playlist> charts) {
        mAdapter.setNewData(charts);
    }

    @Override
    public void showList(SpecailRadioBean data) {

    }

    @Override
    public void showMusicList(QueryRadioAudioListBean data) {

    }

      @Override
    public void getDBMusicList(List<com.cyl.musiclake.bean.Music> muList) {

    }

}