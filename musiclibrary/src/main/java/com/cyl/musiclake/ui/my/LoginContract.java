package com.cyl.musiclake.ui.my;

import android.app.Activity;
import android.content.Intent;

import com.cyl.musiclake.base.BaseContract;
import com.cyl.musiclake.ui.my.user.User;

import java.util.Map;

/**
 * Created by D22434 on 2018/1/3.
 */

public interface LoginContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(String msg);

        void success(User user);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void login(Map<String, String> params);

        void loginByQQ(Activity activity);

        void onActivityResult(int requestCode, int resultCode, Intent data);

    }

}
