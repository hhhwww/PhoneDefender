package com.xd.phonedefender.hw.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by hhhhwei on 16/2/15.
 */
public class FlowInfo {
    private Drawable appIcon;
    private String appName;

    private String upFlow;
    private String downFlow;
    private String allFlow;

    public String getAllFlow() {
        return allFlow;
    }

    public void setAllFlow(String allFlow) {
        this.allFlow = allFlow;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(String upFlow) {
        this.upFlow = upFlow;
    }

    public String getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(String downFlow) {
        this.downFlow = downFlow;
    }
}
