package com.cyl.musiclake.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.cyl.musiclake.MusicApp;


/**
 * 提示工具类
 */
public class ToastUtils {
    private static Toast toast;

    public static void show(Context context, String info) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(String info) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(MusicApp.getAppContext(), info, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showCenter(String info) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(MusicApp.getAppContext(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void show(Context context, int info) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, info + "", Toast.LENGTH_SHORT);
        toast.show();
    }
}
