package com.leben.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import com.leben.base.config.AppConfig;

public class BaseApplication extends Application {
    // 提供一个静态的 Context，方便在类似 Utils 里使用
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

}
