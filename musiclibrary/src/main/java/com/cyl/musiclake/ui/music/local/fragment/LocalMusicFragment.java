package com.cyl.musiclake.ui.music.local.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cyl.musiclake.R;
import com.cyl.musiclake.R2;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.common.Extras;
import com.cyl.musiclake.ui.main.PageAdapter;

import butterknife.BindView;

/**
 * Created by Monkey on 2015/6/29.
 */
public class LocalMusicFragment extends BaseLazyFragment {
    @BindView(R2.id.m_viewpager)
    ViewPager viewPager;
    @BindView(R2.id.tabs)
    TabLayout mTabLayout;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    public static LocalMusicFragment newInstance(String flag) {
        Bundle args = new Bundle();
        args.putString(Extras.SONG_DB, flag);
        LocalMusicFragment fragment = new LocalMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_music;
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(getResources().getString(R.string.local_music));
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
        adapter.addFragment(SongsFragment.Companion.newInstance(), "本地");
        adapter.addFragment(AlbumFragment.newInstance(), "专辑");
        adapter.addFragment(ArtistFragment.newInstance(), "艺术家");
        adapter.addFragment(FoldersFragment.Companion.newInstance(), "文件夹");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onLazyLoad() {
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
    }
}
