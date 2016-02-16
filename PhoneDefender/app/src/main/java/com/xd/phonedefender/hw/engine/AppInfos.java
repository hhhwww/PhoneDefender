package com.xd.phonedefender.hw.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.xd.phonedefender.hw.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/1/29.
 */
public class AppInfos {

    public static List<AppInfo> getAppInfos(Context context) {

        List<AppInfo> lists = new ArrayList<>();

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedPackages = packageManager.getInstalledApplications(0);

        for (ApplicationInfo a : installedPackages) {

            AppInfo appInfo = new AppInfo();

            Drawable apkIcon = a.loadIcon(packageManager);
            String apkName = a.loadLabel(packageManager).toString();
            String apkPackageName = a.packageName;

            //获取apk的安装路径->获取apk的大小
            String sourceDir = a.sourceDir;
            File file = new File(sourceDir);
            long apkSize = file.length();

            appInfo.setApkIcon(apkIcon);
            appInfo.setApkName(apkName);
            appInfo.setApkPackageName(apkPackageName);
            appInfo.setApkSize(apkSize);

            /**
             * 开始写判断是用户程序还是系统程序，程序在内存中还是在SD卡中
             */
            int flags = a.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                appInfo.setUserApp(false);
            else
                appInfo.setUserApp(true);

            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0)
                appInfo.setIsRom(false);
            else
                appInfo.setIsRom(true);

            lists.add(appInfo);
        }

        return lists;
    }

}
