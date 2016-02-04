package com.xd.phonedefender.hw.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xd.phonedefender.hw.utils.ServiceStatusUtils;

/**
 * Created by hhhhwei on 16/2/4.
 */
public class ScreenOffReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceStatusUtils.killAllProcess(context);
    }

}
