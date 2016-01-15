package com.xd.phonedefender.hw.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by hhhhwei on 16/1/15.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private SharedPreferences sp;
    private TelephonyManager telephonyManager;
    private String currentSim;
    private String savedSim;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        currentSim = telephonyManager.getSimSerialNumber();
        savedSim = sp.getString("sim", null);

        System.out.println("FFFFFFFFFFFFFFFFFF");

        if (savedSim.equals(currentSim))
            Log.d("HW_LOG", "手机安全");
        else
            //处理手机不安全的逻辑
            Log.d("HW_LOG", "手机不安全");
    }

}
