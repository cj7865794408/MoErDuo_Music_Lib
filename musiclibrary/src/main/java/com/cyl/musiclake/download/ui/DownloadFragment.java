package com.cyl.musiclake.download.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.R2;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.ui.main.PageAdapter;

import butterknife.BindView;

/**
 * Created by yonglong on 2016/11/26.
 */

public class DownloadFragment extends BaseLazyFragment {
    @BindView(R2.id.m_viewpager)
    ViewPager viewPager;
    @BindView(R2.id.tabs)
    TabLayout mTabLayout;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    public static DownloadFragment newInstance() {
        Bundle args = new Bundle();
        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(getResources().getString(R.string.item_download));
        if (getActivity() != null) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
            appCompatActivity.setSupportActionBar(mToolbar);
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initInjector() {

    }

    private void setupViewPager(ViewPager viewPager) {
        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
        adapter.addFragment(DownloadedFragment.newInstance(), getString(R.string.download_complete));
        adapter.addFragment(DownloadManagerFragment.Companion.newInstance(), getString(R.string.download_processing));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onLazyLoad() {
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
    }
}
