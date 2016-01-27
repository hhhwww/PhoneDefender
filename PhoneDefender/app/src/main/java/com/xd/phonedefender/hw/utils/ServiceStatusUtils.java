package com.xd.phonedefender.hw.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by hhhhwei on 16/1/26.
 */
public class ServiceStatusUtils {
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(serviceName))
                return true;
        }
        return false;
    }
}
