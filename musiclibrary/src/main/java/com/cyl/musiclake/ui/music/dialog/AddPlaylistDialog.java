package com.cyl.musiclake.ui.music.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.bean.Music;

/**
 * 作者：yonglong on 2016/9/14 15:24
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
@SuppressWarnings("ALL")
public class AddPlaylistDialog extends DialogFragment {

    private static String TAG_CREATE = "create_playlist";
    private static boolean result = false;

    public static AddPlaylistDialog newInstance(Music song) {
        AddPlaylistDialog dialog = new AddPlaylistDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", song);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Music music = getArguments().getParcelable("music");
        if (music.getType() == Constants.LOCAL) {
            return showInfoDialog("增加到歌单", "暂不支持添加本地歌曲到在线歌单");
        }
        return null;
    }


    public MaterialDialog showInfoDialog(String title, String msg) {
        if (title.isEmpty()) {
            title = "提示";
        }
        if (msg.isEmpty()) {
            msg = "加载中";
        }
        return new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(msg)
                .positiveText(R.string.sure)
                .onPositive(null)
                .build();
    }

}