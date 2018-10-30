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
        String token = "c3c1c3e31dd14f499caacb2d2b3aefea";
        String MEDStudentId = "bba5be2582ed4796bc00cc791521b9e5";
        String url = "http://192.168.1.181:8050/ParentServer/";
        textView.setOnClickListener(view -> NavigationHelper.INSTANCE.navigateToPlaying(MusicMainActivity.this, null, token, MEDStudentId, url));
        setContentView(textView);

//        String token = "659313cc775541849851a6681b9ea24f";
//        String MEDStudentId = "30e9902ba7484682aa1bd152c6fe9338";
//        String url = "http://test.api.p.ajia.cn:8080/ajiau-api/ParentServer/";

//        NavigationHelper.INSTANCE.navigateToPlaying(MusicMainActivity.this, null, token, MEDStudentId, url);
    }
}