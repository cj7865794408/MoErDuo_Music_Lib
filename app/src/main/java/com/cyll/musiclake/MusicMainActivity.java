package com.cyll.musiclake;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;

import com.cyl.musiclake.common.NavigationHelper;

public class MusicMainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(40);
        textView.setText("点击我，关闭应用");
        textView.setOnClickListener(view -> finish());
        setContentView(textView);
        String token = "c3c1c3e31dd14f499caacb2d2b3aefea";
        String MEDStudentId = "bba5be2582ed4796bc00cc791521b9e5";
        String url = "http://192.168.1.181:8050/ParentServer/";
        NavigationHelper.INSTANCE.navigateToPlaying(MusicMainActivity.this, null, token, MEDStudentId, url);
    }
}