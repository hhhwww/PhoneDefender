package com.xd.phonedefender.hw.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.receiver.AppWidgetReceiver;
import com.xd.phonedefender.hw.utils.ServiceStatusUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hhhhwei on 16/2/5.
 */
public class KillProcessWidgetService extends Service {

    private AppWidgetManager appWidgetManager;
    private ComponentName componentName;
    private RemoteViews remoteView;

    private PendingIntent pendingIntent;
    private Intent intent;

    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        appWidgetManager = AppWidgetManager.getInstance(this);
        componentName = new ComponentName(this, AppWidgetReceiver.class);
        remoteView = new RemoteViews(getPackageName(), R.layout.process_widget);

        intent = new Intent(this, KillAllProcessWidgetReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                remoteView.setTextViewText(R.id.process_count, "正在运行的软件:"
                        + ServiceStatusUtils.getRunningService(getApplicationContext()));

                remoteView.setTextViewText(R.id.process_memory, "可用内存:"
                        + Formatter.formatFileSize(getApplicationContext()
                        , ServiceStatusUtils.getAvailMemory(getApplicationContext())));

                remoteView.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);

                appWidgetManager.updateAppWidget(componentName, remoteView);
            }
        };
        timer.schedule(timerTask, 0, 5000);

        super.onCreate();
    }

}
