package com.cyl.musiclake.ui.my;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseFragment;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.ui.my.user.UserStatus;

/**
 * 作者：yonglong on 2016/8/8 17:47
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 用户fragment :主要包括个人信息
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {

    private TextView nick, email, phone;

    private CardView user_logout;

    private User user;

    public static UserFragment newInstance(int flag) {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void listener() {
        user_logout.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        if (UserStatus.getstatus(getActivity())) {

            user = UserStatus.getUserInfo(getActivity());
            if (user.getNick() == null || user.getNick().length() <= 0) {
                nick.setText("");
            } else {
                nick.setText(user.getNick());
            }
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
        }
    }

    @Override
    public int getLayoutId() {

        return R.layout.frag_user;
    }

    @Override
    public void initViews() {

        nick = (TextView) rootView.findViewById(R.id.nick);
        email = (TextView) rootView.findViewById(R.id.email);
        phone = (TextView) rootView.findViewById(R.id.phone);

        user_logout = (CardView) rootView.findViewById(R.id.logout);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.logout) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("是否注销？")
                    .setMessage("注销后不能享有更多功能！")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            logout();
                        }
                    })
                    .show();

        }
    }

    private void logout() {
        UserStatus.clearUserInfo(getActivity());
        UserStatus.saveuserstatus(getActivity(), false);
        getActivity().finish();
    }
}

