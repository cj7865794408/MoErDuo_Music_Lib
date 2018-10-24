package com.cyl.musiclake.ui.my;

import com.cyl.musiclake.base.BaseContract;

/**
 * Created by D22434 on 2018/1/3.
 */

public interface RegisterContract {

    interface View extends BaseContract.BaseView {
        void showErrorInfo(int flag, String msg);

        void showSuccessInfo(String msg);

        void updateView();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void register(String email, String username, String password);
    }

}
