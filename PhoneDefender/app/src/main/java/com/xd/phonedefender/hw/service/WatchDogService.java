package com.xd.phonedefender.hw.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xd.phonedefender.hw.activity.EnterPwdActivity;
import com.xd.phonedefender.hw.db.dao.AppLockDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/13.
 */
public class WatchDogService extends Service {
    private ActivityManager activityManager;
    private AppLockDao appLockDao;

    private IntentFilter intentFilter;
    private ScreenOffOnReceiver screenOffOnReceiver;

    private List<String> mDatas;

    private boolean flag = false;

    private String tempPackageName;

    private MyReceiver myReceiver;
    private IntentFilter intentFilter2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        appLockDao = new AppLockDao(this);

        mDatas = new ArrayList<>();
        mDatas = appLockDao.findAll();

        getContentResolver().registerContentObserver(Uri.parse("content://hewei"), true,
                new MyContentObserver(new Handler()));

        startWatch();

        register();
    }

    private void startWatch() {
        flag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
                    String packageName = runningTasks.get(0).topActivity.getPackageName();
                    if (mDatas.contains(packageName)) {
                        if (!packageName.equals(tempPackageName)) {
                            Intent intent = new Intent(WatchDogService.this, EnterPwdActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("packageName", packageName);
                            startActivity(intent);
                        } else {

                        }
                    } else {
                    }
                }
            }
        }).start();
    }

    private void register() {
        intentFilter = new IntentFilter();
        screenOffOnReceiver = new ScreenOffOnReceiver();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffOnReceiver, intentFilter);

        myReceiver = new MyReceiver();
        intentFilter2 = new IntentFilter();
        intentFilter2.addAction("hewei.abort");
        registerReceiver(myReceiver, intentFilter2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOffOnReceiver);
        unregisterReceiver(myReceiver);
        flag = false;
    }


    private class ScreenOffOnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_SCREEN_ON:
                    startWatch();
                    break;

                case Intent.ACTION_SCREEN_OFF:
                    flag = false;
                    break;
            }
        }
    }//receiver

    private class MyContentObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mDatas = appLockDao.findAll();
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null)
                tempPackageName = intent.getStringExtra("packageName");
        }
    }
}
