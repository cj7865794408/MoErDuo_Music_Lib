package com.cyll.musiclake;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.cyl.musiclake.MusicApp;

public class AppNewLication extends MusicApp {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
