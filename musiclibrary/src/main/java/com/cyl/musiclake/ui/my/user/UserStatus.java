package com.cyl.musiclake.ui.my.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.cyl.musiclake.common.Constants;

/**
 * Created by 永龙 on 2015/12/22.
 */
public class UserStatus {
    //保存个人信息到user.xml文件中
    public static void saveUserInfo(Context context, User userInfo) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.USER_ID, userInfo.getId());
        editor.putString(Constants.PASSWORD, userInfo.getToken());
        editor.putString(Constants.USERNAME, userInfo.getName());
        editor.putString(Constants.USER_SEX, userInfo.getSex());
        editor.putString(Constants.USER_COLLEGE, userInfo.getCollege());
        editor.putString(Constants.USER_MAJOR, userInfo.getMajor());
        editor.putString(Constants.USER_CLASS, userInfo.getClasses());

        editor.putString(Constants.USER_IMG, userInfo.getAvatar());
        editor.putString(Constants.USER_EMAIL, userInfo.getEmail());
        editor.putString(Constants.PHONE, userInfo.getPhone());
        editor.putString(Constants.NICK, userInfo.getNick());
        editor.putInt(Constants.SECRET, userInfo.getSecret());

        editor.apply();
        saveuserstatus(context, true);
    }

    //从data.xml文件中取出个人信息
    public static User getUserInfo(Context context) {
        if (!getstatus(context)) return null;
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        User user = new User();
        user.setId(sp.getString(Constants.USER_ID, null));
        user.setName(sp.getString(Constants.USERNAME, null));
        user.setToken(sp.getString(Constants.PASSWORD, null));
        user.setSex(sp.getString(Constants.USER_SEX, null));
        user.setCollege(sp.getString(Constants.USER_COLLEGE, null));
        user.setMajor(sp.getString(Constants.USER_MAJOR, null));
        user.setClasses(sp.getString(Constants.USER_CLASS, null));
        user.setAvatar(sp.getString(Constants.USER_IMG, null));
        user.setEmail(sp.getString(Constants.USER_EMAIL, null));
        user.setPhone(sp.getString(Constants.PHONE, null));
        user.setNick(sp.getString(Constants.NICK, null));
        user.setSecret(sp.getInt(Constants.SECRET, 0));
        return user;
    }

    //从data.xml文件中清空个人信息
    public static void clearUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        saveuserstatus(context, false);
    }

    //登录状态、
    public static void saveuserstatus(Context context, boolean status) {
        SharedPreferences sp = context.getSharedPreferences("status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("status", status);
        editor.apply();
    }

    public static boolean getstatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("status", Context.MODE_PRIVATE);
        return sp.getBoolean("status", false);
    }

}
