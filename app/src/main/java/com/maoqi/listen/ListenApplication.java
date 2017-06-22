package com.maoqi.listen;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by maoqi on 2017/6/15.
 */

public class ListenApplication extends Application {
    public static Context appContext;
    //维护一个线程池
    public static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    @Override
    public void onTerminate() {
        Log.d("ListenApplication", "退出了");
        super.onTerminate();
    }
}
