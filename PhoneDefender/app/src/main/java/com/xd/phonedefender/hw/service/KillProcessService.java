package com.xd.phonedefender.hw.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xd.phonedefender.hw.receiver.ScreenOffReceiver;

/**
 * Created by hhhhwei on 16/2/4.
 */
public class KillProcessService extends Service {

    private ScreenOffReceiver screenOffReceiver;
    private IntentFilter intentFilter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        screenOffReceiver = new ScreenOffReceiver();
        intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOffReceiver);
    }
}
