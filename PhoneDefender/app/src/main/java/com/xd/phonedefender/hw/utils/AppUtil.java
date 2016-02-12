package com.xd.phonedefender.hw.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhhhwei on 16/2/12.
 */
public class AppUtil {

    public static List<String> getAppNames(Context context) {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        for (ApplicationInfo a : installedApplications) {
            String name = a.loadLabel(packageManager).toString();
            names.add(name);
        }
        return names;
    }

}
