package com.xd.phonedefender.hw.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

import com.xd.phonedefender.hw.db.AddressDao;

/**
 * Created by hhhhwei on 16/1/26.
 */
public class AdressService extends Service {

    private TelephonyManager tm;
    private MyPhoneStateListener myPhoneStateListener;
    private OOutCallReceiver oOutCallReceiver;
    private IntentFilter intentFilter;

    private WindowManager windowManager;
    private TextView view;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();

        tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        oOutCallReceiver = new OOutCallReceiver();
        intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(oOutCallReceiver, intentFilter);
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String address = AddressDao.getAddress(incomingNumber);
                    showToast(address);
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if (windowManager != null && view != null)
                        windowManager.removeViewImmediate(view);
                    break;
            }
        }
    }//MyPhoneStateListener

    class OOutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDao.getAddress(number);
            showToast(address);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(oOutCallReceiver);
    }

    private void showToast(String text) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//***
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSPARENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
//***
        view = new TextView(this);

//        view.setBackgroundResource(); + 一个整形数组 + 一个sharedPreference
        view.setText(text);
        view.setTextColor(Color.RED);

        windowManager.addView(view, params);
    }
}
