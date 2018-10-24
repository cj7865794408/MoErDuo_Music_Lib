package com.cyl.musiclake.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.R2;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.ui.music.discover.DiscoverFragment;
import com.cyl.musiclake.ui.music.local.fragment.MyMusicFragment;
import com.cyl.musiclake.ui.music.mv.MvFragment;
import com.cyl.musiclake.ui.music.online.fragment.NeteasePlaylistFragment;

import butterknife.BindView;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ConstantConditions")
public class MainFragment extends BaseFragment {
    @BindView(R2.id.m_viewpager)
    ViewPager mViewPager;
    @BindView(R2.id.tabs)
    TabLayout mTabLayout;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initViews() {
        if (getActivity() != null) {
            mToolbar.setTitle(R.string.app_name);
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            final ActionBar toggle = appCompatActivity.getSupportActionBar();
            if (toggle != null) {
                toggle.setHomeAsUpIndicator(R.drawable.ic_menu_white);
                toggle.setDisplayHomeAsUpEnabled(true);
            }
        }
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        setupViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void initInjector() {

    }

    private void setupViewPager(ViewPager mViewPager) {
        PageAdapter mAdapter = new PageAdapter(getChildFragmentManager());
        mAdapter.addFragment(MyMusicFragment.Companion.newInstance(), "我的");
        mAdapter.addFragment(DiscoverFragment.Companion.newInstance(), "发现");
        mAdapter.addFragment(NeteasePlaylistFragment.Companion.newInstance(), "排行榜");
        mAdapter.addFragment(MvFragment.newInstance(), "MV");
//        mAdapter.addFragment(MvListFragment.newInstance("recently"), "最新MV");
        mViewPager.setAdapter(mAdapter);
    }

}

