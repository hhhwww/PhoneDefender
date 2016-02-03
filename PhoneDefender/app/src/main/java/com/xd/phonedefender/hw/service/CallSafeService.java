package com.xd.phonedefender.hw.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.xd.phonedefender.hw.db.BlackNumberDao;

/**
 * Created by hhhhwei on 16/1/29.
 */
public class CallSafeService extends Service {

    private InnerReceiver innerReceiver;
    private IntentFilter intentFilter;
    private BlackNumberDao blackNumberDao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blackNumberDao = new BlackNumberDao(this);
        innerReceiver = new InnerReceiver();
        intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(innerReceiver);
    }

    class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object o : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) o);
                String originatingAddress = message.getOriginatingAddress();
                String mode = blackNumberDao.find(originatingAddress);
                if (!TextUtils.isEmpty(mode) && (mode.equals("1") || mode.equals("3"))) {
                    abortBroadcast();
                }
                //智能拦截，从数据库中查询相似的内容进行拦截
            }
        }

    }//



}
