package com.xd.phonedefender.hw.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xd.phonedefender.hw.db.dao.AddressDao;
import com.xd.phonedefender.hw.utils.ToastUtil;

/**
 * Created by hhhhwei on 16/1/26.
 */
public class OutCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String number = getResultData();
        String address = AddressDao.getAddress(number);
        ToastUtil.showMessage(address);
    }

}
