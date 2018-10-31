package com.cyl.musiclake.ui.settings;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cyl.musiclake.BuildConfig;
import com.cyl.musiclake.R;
import com.cyl.musiclake.R2;
import com.cyl.musiclake.base.BaseActivity;
import com.cyl.musiclake.utils.Tools;
import com.cyl.musiclake.view.FlipperView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cyl.musiclake.common.Constants.ABOUT_MUSIC_LAKE_URL;

/**
 * Created by lw on 2018/2/12.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R2.id.flipperView)
    FlipperView flipperView;
    @BindView(R2.id.tv_about_version)
    TextView mVersion;
    @BindView(R2.id.cardEmailView)
    View cardEmailView;
    @BindView(R2.id.logoFab)
    FloatingActionButton mLogoFab;
    @BindView(R2.id.shareFab)
    FloatingActionButton shareFab;
    @BindView(R2.id.aboutContainerView)
    View mView;
    ObjectAnimator animator;

    @OnClick(R2.id.cardGithubView)
    void introduce() {
        Tools.INSTANCE.openBrowser(this, ABOUT_MUSIC_LAKE_URL);
    }

    @OnClick(R2.id.logoFab)
    void toFlipper() {
        flipperView.setOnClick();
    }

    @OnClick(R2.id.cardEmailView)
    void toFeedback() {
        Tools.INSTANCE.feeback(this);
    }

    @OnClick(R2.id.shareFab)
    void toShare() {
        revealRed();
    }

    @OnClick(R2.id.cardUpdateView)
    void toUpdate() {
//        Beta.checkUpgrade();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        mView.startAnimation(animation1);

        animator = ObjectAnimator.ofFloat(mLogoFab, "scaleX", 1.1f, 0.9f);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setDuration(800);
        animator.addUpdateListener(animation -> {
            float x = (float) animation.getAnimatedValue();
            mLogoFab.setScaleY(x);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                flipperView.setOnClick();
            }
        });
        animator.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        animator.cancel();
    }

    @Override
    protected void initData() {
        mVersion.setText(String.format("版本号v%s", BuildConfig.VERSION_NAME));
    }

    @Override
    protected void initInjector() {

    }

    private void revealRed() {
        // 保存最开始的状态的参数
        final ViewGroup.LayoutParams saveParams = shareFab.getLayoutParams();
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition
                .changebounds_with_arcmotion);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }


            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealColor(mView.findViewById(R.id.ll_layout), R.color.colorAccent);
                // 动画结束之后，将 红圈再设回以前的参数
                shareFab.setLayoutParams(saveParams);
                Tools.INSTANCE.share(AboutActivity.this);
            }


            @Override
            public void onTransitionCancel(Transition transition) {

            }


            @Override
            public void onTransitionPause(Transition transition) {

            }


            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        // 保存 每个 View 当前的可见状态(Visibility)。
        TransitionManager.beginDelayedTransition(mView.findViewById(R.id.ll_layout), transition);

        // 移动红圈到中央
//        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
//                .WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
//        cardEmailView.setLayoutParams(layoutParams1);
    }

    private void animateRevealColor(ViewGroup viewRoot, @ColorRes int color) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        animateRevealColorFromCoordinates(viewRoot, color, cx, cy);
    }


    private void animateRevealColorFromCoordinates(ViewGroup viewRoot, @ColorRes int color, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        @SuppressLint({"NewApi", "LocalSuppress"}) Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, color));
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }


}
