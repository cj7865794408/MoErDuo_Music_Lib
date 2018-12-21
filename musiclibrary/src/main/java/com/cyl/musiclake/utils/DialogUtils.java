package com.cyl.musiclake.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyl.musiclake.R;

/**
 * Created by master on 2018/4/7.
 */

public class DialogUtils {

    private static AlertDialog mProgressDialog;

    public static final void showResultDialog(Context context, String msg,
                                              String title) {
        if (msg == null) return;
        String rmsg = msg.replace(",", "\n");
        LogUtil.d("Util", rmsg);
        new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
                .setNegativeButton("知道了", null).create().show();
    }

    public static final void showProgressDialog(Context context, String title,
                                                String message) {
        dismissDialog();
        if (TextUtils.isEmpty(title)) {
            title = "请稍候";
        }
        if (TextUtils.isEmpty(message)) {
            message = "正在加载...";
        }
        mProgressDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setView(new ProgressBar(context))
                .create();
    }

    public static void showConfirmCancelDialog(Context context, String message) {
//        AlertDialog dlg = new AlertDialog.Builder(context).setMessage(message)
//                .setPositiveButton("确认", posListener)
//                .setNegativeButton("取消", null).create();
//        dlg.setCanceledOnTouchOutside(false);
//        dlg.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View
                .inflate(context, R.layout.login_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        //取消或确定按钮监听事件处理
        AlertDialog dialog = builder.create();
        TextView title_v= (TextView) view
                .findViewById(R.id.title);//设置标题
        TextView input_edt= (TextView) view
                .findViewById(R.id.dialog_edit);//输入内容
        Button btn_cancel=(Button)view
                .findViewById(R.id.btn_cancel);//取消按钮
        Button btn_comfirm=(Button)view
                .findViewById(R.id.btn_comfirm);//确定按钮
        title_v.setText("系统提示");
        input_edt.setText(""+message);
        btn_cancel.setOnClickListener(view1 -> dialog.dismiss());
        btn_comfirm.setOnClickListener(view12 -> {
            dialog.dismiss();
            UpdateUtils.notrftyStudentApp();
            ((Activity) context).finish();

        });
        dialog.show();

    }



    public static final void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
