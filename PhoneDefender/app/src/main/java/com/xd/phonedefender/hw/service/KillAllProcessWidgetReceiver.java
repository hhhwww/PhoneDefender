package com.xd.phonedefender.hw.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;

import com.xd.phonedefender.hw.utils.ServiceStatusUtils;
import com.xd.phonedefender.hw.utils.ToastUtil;

/**
 * Created by hhhhwei on 16/2/5.
 */
public class KillAllProcessWidgetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long availMemory1 = ServiceStatusUtils.getAvailMemory(context);
        int runningService1 = ServiceStatusUtils.getRunningService(context);
        ServiceStatusUtils.killAllProcess(context);
        int runningService2 = ServiceStatusUtils.getRunningService(context);
        long availMemory2 = ServiceStatusUtils.getAvailMemory(context);

        int count1 = runningService1 - runningService2;
        long count2 = availMemory2 - availMemory1;

        String s = Formatter.formatFileSize(context, count2);

        ToastUtil.showMessage("清理成功，清理了" + count1 + "个程序," + s + "空间");
    }
}
