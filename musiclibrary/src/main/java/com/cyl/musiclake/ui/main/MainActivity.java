package com.cyl.musiclake.ui.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.R2;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.bean.Music;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.event.LoginEvent;
import com.cyl.musiclake.event.MetaChangedEvent;
import com.cyl.musiclake.event.PlaylistEvent;
import com.cyl.musiclake.player.PlayManager;
import com.cyl.musiclake.ui.map.ShakeActivity;
import com.cyl.musiclake.ui.music.player.PlayControlFragment;
import com.cyl.musiclake.ui.music.search.SearchActivity;
import com.cyl.musiclake.ui.my.LoginActivity;
import com.cyl.musiclake.ui.my.user.UserStatus;
import com.cyl.musiclake.ui.settings.AboutActivity;
import com.cyl.musiclake.ui.settings.SettingsActivity;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.utils.Tools;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cyl.musiclake.ui.UIUtilsKt.logout;
import static com.cyl.musiclake.ui.UIUtilsKt.updateLoginToken;

/**
 * 描述 主要的Activity
 *
 * @author yonglong
 * @date 2016/8/3
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R2.id.sliding_layout)
    public SlidingUpPanelLayout mSlidingUpPaneLayout;
    @BindView(R2.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    public ImageView mImageView;
    CircleImageView mAvatarIcon;
    TextView mName;
    TextView mNick;

    private View headerView;
    private PlayControlFragment controlFragment;
    private static final String TAG = "MainActivity";

    private boolean mIsLogin = false;

    Class<?> mTargetClass = null;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        transparentStatusBar(this);
        //菜单栏的头部控件初始化
        initNavView();
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        checkLoginStatus();
    }


    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMetaChangedEvent(MetaChangedEvent event) {
        updatePlaySongInfo(event.getMusic());
    }

    private void updatePlaySongInfo(Music music) {
        if (mSlidingUpPaneLayout == null) return;
        if (music != null) {
            mSlidingUpPaneLayout.setPanelHeight(getResources().getDimensionPixelOffset(R.dimen.dp_56));
            CoverLoader.loadBigImageView(this, music, mImageView);
        } else {
            mSlidingUpPaneLayout.setPanelHeight(0);
            mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
        }
    }

    private void initNavView() {
        headerView = mNavigationView.getHeaderView(0);
        mImageView = headerView.findViewById(R.id.header_bg);
        mAvatarIcon = headerView.findViewById(R.id.header_face);
        mName = headerView.findViewById(R.id.header_name);
        mNick = headerView.findViewById(R.id.header_nick);
    }

    @Override
    protected void initData() {
        String from = getIntent().getAction();
        if (from != null && from.equals(Constants.DEAULT_NOTIFICATION)) {
            mSlidingUpPaneLayout.setPanelHeight(getResources().getDimensionPixelOffset(R.dimen.dp_56));
            mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
        }
        updatePlaySongInfo(PlayManager.getPlayingMusic());
        //加载主fragment
        navigateLibrary.run();
        navigatePlay.run();
    }

    @Override
    protected void initInjector() {
    }


    @Override
    protected void listener() {
        mSlidingUpPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                LogUtil.d(TAG, "onPanelStateChanged " + newState);
                if (newState == PanelState.EXPANDED) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                LogUtil.d(TAG, "onPanelSlide, offset " + slideOffset);
                if (controlFragment.topContainer != null) {
                    controlFragment.topContainer.setAlpha(1 - slideOffset * 2);
                    if (controlFragment.topContainer.getAlpha() < 0) {
                        controlFragment.topContainer.setVisibility(View.GONE);
                    } else {
                        controlFragment.topContainer.setVisibility(View.VISIBLE);
                        mSlidingUpPaneLayout.setTouchEnabled(true);
                    }
                }
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (mTargetClass != null) {
                    turnToActivity(mTargetClass);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 菜单条目点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.nav_login_status) {
            if (mIsLogin) {
                new MaterialDialog.Builder(this)
                        .title("音乐湖")
                        .content("您确定要退出或切换其他账号吗？")
                        .positiveText("确定")
                        .onPositive((materialDialog, dialogAction) -> {
                            logout();
                        }).negativeText("取消").show();
            } else {
                mTargetClass = LoginActivity.class;
            }
            mDrawerLayout.closeDrawers();

        } else if (i == R.id.nav_menu_shake) {
            item.setChecked(true);
            if (!mIsLogin) {
                mTargetClass = LoginActivity.class;
            } else {
                mTargetClass = ShakeActivity.class;
            }

        } else if (i == R.id.nav_menu_playQueue) {
            NavigationHelper.INSTANCE.navigatePlayQueue(this);

        } else if (i == R.id.nav_menu_setting) {
            mTargetClass = SettingsActivity.class;

        } else if (i == R.id.nav_menu_feedback) {
            Tools.INSTANCE.feeback(this);

        } else if (i == R.id.nav_menu_about) {
            mTargetClass = AboutActivity.class;

        } else if (i == R.id.nav_menu_equalizer) {
            try {
                Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, PlayManager.getAudioSessionId());
                startActivityForResult(effects, 666);
            } catch (Exception e) {
                ToastUtils.show("设备不支持均衡！");
            }

        } else if (i == R.id.nav_menu_exit) {
            mTargetClass = null;
            finish();

        }
        mDrawerLayout.closeDrawers();
        return false;
    }

    /**
     * 跳转Activity
     *
     * @param cls
     */
    private void turnToActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
        mTargetClass = null;
    }

    private Runnable navigateLibrary = () -> {
        Fragment fragment = MainFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();

    };

    private Runnable navigatePlay = () -> {
        controlFragment = PlayControlFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.controls_container, controlFragment).commit();
    };


    //返回键
    @Override
    public void onBackPressed() {
        if (mSlidingUpPaneLayout != null &&
                (mSlidingUpPaneLayout.getPanelState() == PanelState.EXPANDED || mSlidingUpPaneLayout.getPanelState() == PanelState.ANCHORED)) {
            mSlidingUpPaneLayout.setPanelState(PanelState.COLLAPSED);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else if (isNavigatingMain()) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            if (isNavigatingMain()) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
            return true;
        } else if (i == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNavigatingMain() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return (currentFragment instanceof MainFragment);
    }


    private void setPlaylistQueueChange() {
//        if (PlayManager.getPlayList().size() == 0) {
//            mSlidingUpPaneLayout.setPanelState(PanelState.HIDDEN);
//        } else if (PlayManager.getPlayList().size() >= 0 && mSlidingUpPaneLayout.getPanelState() == PanelState.HIDDEN) {
//            mSlidingUpPaneLayout.setPanelState(PanelState.EXPANDED);
//        }
    }

    /**
     * 设置用户状态信息
     */
    private void setUserStatusInfo() {
        mIsLogin = UserStatus.getstatus(this);
        if (mIsLogin) {
            String url = UserStatus.getUserInfo(this).getAvatar();
            CoverLoader.loadImageView(this, url, R.drawable.ic_account_circle, mAvatarIcon);
            mName.setText(UserStatus.getUserInfo(this).getNick());
            mNick.setText(getResources().getString(R.string.app_name));
            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle(getResources().getString(R.string.logout_hint))
                    .setIcon(R.drawable.ic_exit);
        } else {
            mAvatarIcon.setImageResource(R.drawable.ic_account_circle);
            mName.setText(getResources().getString(R.string.app_name));
            mNavigationView.getMenu().findItem(R.id.nav_login_status).setTitle(getResources().getString(R.string.login_hint));
            mNick.setText(getResources().getString(R.string.login_hint));
        }
    }

    /**
     * 登陆成功重新设置用户新
     *
     * @param event
     */
    @Subscribe
    public void updateUserInfo(LoginEvent event) {
        setUserStatusInfo();
    }

    /**
     * 更新歌单
     *
     * @param event
     */
    @Subscribe
    public void updatePlaylist(PlaylistEvent event) {
        if (event.getType().equals(Constants.PLAYLIST_QUEUE_ID)) {
            setPlaylistQueueChange();
        } else if (event.getType().equals(Constants.PLAYLIST_LOVE_ID)) {
            Music music = PlayManager.getPlayingMusic();
            if (music != null && music.isLove()) {
                controlFragment.mIvLove.setImageResource(R.drawable.item_favorite_love);
            } else if (music != null && !music.isLove()) {
                controlFragment.mIvLove.setImageResource(R.drawable.item_favorite);
            }
        }
    }

    /**
     * 检查QQ登录状态
     */
    private void checkLoginStatus() {
        String token = SPUtils.getAnyByKey(SPUtils.QQ_ACCESS_TOKEN, "");
        String openId = SPUtils.getAnyByKey(SPUtils.QQ_OPEN_ID, "");
        if (token.length() > 0 && openId.length() > 0) {
            updateLoginToken(token, openId);
        } else {
            EventBus.getDefault().post(new PlaylistEvent(Constants.PLAYLIST_CUSTOM_ID));
        }
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
