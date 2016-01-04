package com.xd.phonedefender.hw.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by hhhhwei on 16/1/4.
 */
public class MyApplication extends Application {

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        if(mContext == null)
            mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
