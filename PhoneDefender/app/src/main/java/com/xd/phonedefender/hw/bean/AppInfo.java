package com.xd.phonedefender.hw.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by hhhhwei on 16/1/29.
 */
public class AppInfo {

    private Drawable apkIcon;

    private String apkName;

    private long apkSize;

    private String apkPackageName;

    /**
     * 用户APP还是系统APP
     */
    private boolean userApp;

    /**
     * APP是在ROM中还是在内存中
     */
    private boolean isRom;

    public Drawable getApkIcon() {
        return apkIcon;
    }

    public void setApkIcon(Drawable apkIcon) {
        this.apkIcon = apkIcon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", apkPackageName='" + apkPackageName + '\'' +
                ", userApp=" + userApp +
                ", isRom=" + isRom +
                '}';
    }

}
