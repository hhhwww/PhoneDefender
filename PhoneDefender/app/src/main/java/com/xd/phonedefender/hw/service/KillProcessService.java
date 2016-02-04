package com.xd.phonedefender.hw.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xd.phonedefender.hw.receiver.ScreenOffReceiver;

import java.util.Timer;
import java.util.TimerTask;

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
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.e("total", "--------1");
            }
        };

        timer.schedule(timerTask, 1000, 1000);

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
