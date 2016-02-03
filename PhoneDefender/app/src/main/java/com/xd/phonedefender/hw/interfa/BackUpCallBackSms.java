package com.xd.phonedefender.hw.interfa;

public interface BackUpCallBackSms {
    public void beforeBackUp(int count);

    public void onBackUp(int progress);
}
