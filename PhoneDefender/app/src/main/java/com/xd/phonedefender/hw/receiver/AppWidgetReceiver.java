package com.xd.phonedefender.hw.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.xd.phonedefender.hw.service.KillProcessWidgetService;

/**
 * Created by hhhhwei on 16/2/5.
 * 继承AppWidgetProvider不要去重写onReceive方法，不然不会调用AppWidget的声明周期中的方法
 */
public class AppWidgetReceiver extends AppWidgetProvider {

//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//    }

    //每次有桌面小空间的时候都会调用
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    //每次删除桌面小控件的时候都会调用
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    //开始第一次一次调用
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        context.startService(new Intent(context, KillProcessWidgetService.class));
    }

    //最后一次调用
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        context.stopService(new Intent(context, KillProcessWidgetService.class));
    }

}
