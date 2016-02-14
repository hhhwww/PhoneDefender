package com.xd.phonedefender.hw.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.xd.phonedefender.hw.db.dao.AddressDao;

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

    private SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sp = getSharedPreferences("config", MODE_PRIVATE);

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

        winWidth = windowManager.getDefaultDisplay().getWidth();
        winHeight = windowManager.getDefaultDisplay().getHeight();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//***
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSPARENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
//***

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = sp.getInt("lastX", 0);
        params.y = sp.getInt("lastY", 0);

        view = new TextView(this);

//        view.setBackgroundResource(); + 一个整形数组 + 一个sharedPreference
        view.setText(text);
        view.setTextColor(Color.RED);

        windowManager.addView(view, params);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) motionEvent.getRawX();
                        int endY = (int) motionEvent.getRawY();

                        int dx = endX - startX;
                        int dy = endY - startY;

                        params.x += dx;
                        params.y += dy;

                        if (params.x < 0)
                            params.x = 0;
                        if (params.y < 0)
                            params.y = 0;

                        if (params.x + view.getWidth() > winWidth)
                            params.x = winWidth - view.getWidth();
                        if (params.y + view.getHeight() > winHeight)
                            params.y = winHeight - view.getHeight();

                        windowManager.updateViewLayout(view, params);

                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
                        sp.edit().putInt("lastX", params.x).commit();
                        sp.edit().putInt("lastY", params.y).commit();
                        break;
                }
                return true;
            }
        });
    }

    private int startX;
    private int startY;


    private int winWidth;
    private int winHeight;
}
