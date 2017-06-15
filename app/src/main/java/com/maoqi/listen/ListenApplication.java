package com.maoqi.listen;

import android.app.Application;
import android.content.Context;

/**
 * Created by maoqi on 2017/6/15.
 */

public class ListenApplication extends Application {
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }
}
