package com.xd.phonedefender.hw.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.interfa.BackUpCallBackSms;
import com.xd.phonedefender.hw.utils.SmsUtils;
import com.xd.phonedefender.hw.utils.ToastUtil;

import net.youmi.android.listener.Interface_ActivityListener;
import net.youmi.android.offers.OffersManager;

/**
 * Created by hhhhwei on 16/1/25.
 */
public class AToolsActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void numberAdressQuery(View view) {
        startActivity(new Intent(this, AddressActivity.class));
    }

    public void backUpSms(View view) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在备份请稍后......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean result = SmsUtils.backUpSms(AToolsActivity.this, new BackUpCallBackSms() {
                        @Override
                        public void beforeBackUp(int count) {
                            progressDialog.setMax(count);
                        }

                        @Override
                        public void onBackUp(int progress) {
                            progressDialog.setProgress(progress);
                        }
                    });
                    if (result)
                        ToastUtil.showMessage("备份成功");
                    else
                        ToastUtil.showMessage("备份失败");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void appLock(View view) {
        startActivity(new Intent(this, AppLockActivity.class));
    }

    public void appMarket(View view) {
        OffersManager.getInstance(this).showOffersWall(
                new Interface_ActivityListener() {

                    /**
                     * 但积分墙销毁的时候，即积分墙的Activity调用了onDestory的时候回调
                     */
                    @Override
                    public void onActivityDestroy(Context context) {
                        Toast.makeText(context, "全屏积分墙退出了", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
