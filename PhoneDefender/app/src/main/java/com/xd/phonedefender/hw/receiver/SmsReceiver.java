package com.xd.phonedefender.hw.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.service.LocationService;
import com.xd.phonedefender.hw.utils.ToastUtil;

/**
 * Created by hhhhwei on 16/1/16.
 */
public class SmsReceiver extends BroadcastReceiver {

    private SharedPreferences sp;

    //lockscreen
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    public void onReceive(Context context, Intent intent) {

        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(context, AdminReceiver.class);

        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        if (!devicePolicyManager.isAdminActive(componentName))
            activeDeviceManager(context);

        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for (Object o : objects) {//短信最多140个字节，超出的话，会分为多条短信发送，所以是一个数组,
            //因为我们的短信的指令很短，所以这个for循环只执行一次
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);

            String address = smsMessage.getOriginatingAddress();
            String body = smsMessage.getMessageBody();

            if ("#*alarm*#".equals(body)) {
                startMusic(context);
                abortBroadcast();
            } else if ("#*location*#".equals(body)) {
                context.startService(new Intent(context, LocationService.class));
                Log.e("location", sp.getString("location", ""));
                abortBroadcast();
            } else if ("#*lockscreen*#".equals(body)) {
                lockScreen();
                abortBroadcast();
            } else if ("#*wipedata*#".equals(body)) {
                wipeData();
                abortBroadcast();
            }
        }
    }

    private void activeDeviceManager(Context context) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "哈哈哈啊哈哈");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void startMusic(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void lockScreen() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();//立即锁屏
        } else
            ToastUtil.showMessage("必须先激活设备管理器");
    }

    private void wipeData() {
        if (devicePolicyManager.isAdminActive(componentName))
            devicePolicyManager.wipeData(0);
        else
            ToastUtil.showMessage("必须先激活设备管理器");
    }

    //卸载的逻辑,学习学习
    public void unInstall(Context context) {
        devicePolicyManager.removeActiveAdmin(componentName);

        //remember!!
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
