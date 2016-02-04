package com.xd.phonedefender.hw.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/4.
 */
public class TaskInfosParse {

    public static List<TaskInfo> getTaskInfos(Context context) {
        List<TaskInfo> lists = new ArrayList<>();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo r : runningAppProcesses) {
            ApplicationInfo applicationInfo = null;
            TaskInfo taskInfo = new TaskInfo();

            String runningPackageName = r.processName;
            try {
                applicationInfo = packageManager.getApplicationInfo(runningPackageName, 0);
            } catch (Exception e) {
                Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[]{r.pid});
                long totalPrivateDirty = memoryInfos[0].getTotalPrivateDirty();

                taskInfo.setMemorySize(totalPrivateDirty);
                taskInfo.setIsUser(false);
                taskInfo.setAppName(r.processName);
                taskInfo.setAppIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
                taskInfo.setPackageName(runningPackageName);
            }

            if (applicationInfo != null) {
                Drawable appIcon = applicationInfo.loadIcon(packageManager);
                String appName = applicationInfo.loadLabel(packageManager).toString();

                int flags = applicationInfo.flags;
                boolean isUser;

                if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                    isUser = false;
                else
                    isUser = true;

                taskInfo.setAppIcon(appIcon);
                taskInfo.setAppName(appName);
                taskInfo.setIsUser(isUser);
                taskInfo.setPackageName(runningPackageName);

                //获取正在运行中的单个程序锁占用的内存大小
                Debug.MemoryInfo[] memoryInfos = activityManager.getProcessMemoryInfo(new int[]{r.pid});
                long totalPrivateDirty = memoryInfos[0].getTotalPrivateDirty();

                taskInfo.setMemorySize(totalPrivateDirty);
            }

            lists.add(taskInfo);
        }
        return lists;
    }
}
