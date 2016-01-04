package com.xd.phonedefender.hw.utils;

import android.widget.Toast;

import com.xd.phonedefender.hw.application.MyApplication;

/**
 * Created by hhhhwei on 16/1/4.
 */
public class ToastUtil {
    public static void showMessage(String message){
        Toast.makeText(MyApplication.getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
