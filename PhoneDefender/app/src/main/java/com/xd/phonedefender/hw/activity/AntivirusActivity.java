package com.xd.phonedefender.hw.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.db.dao.AntivirusDao;
import com.xd.phonedefender.hw.utils.MD5Utils;

import java.util.List;

/**
 * Created by hhhhwei on 16/2/11.
 */
public class AntivirusActivity extends AppCompatActivity {

    private ImageView ivScanning;
    private TextView tvScanning;
    private ProgressBar pbScanning;
    private LinearLayout llContent;
    private ScrollView sv;

    private List<String> nameLists;

    private static final int START = 1;
    private static final int SCANNING = 2;
    private static final int FINISH = 3;

    private int progress = 0;
    private Message message;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    tvScanning.setText("初始化引擎中");
                    break;

                case SCANNING:
                    tvScanning.setText("开始疯狂查杀中");

                    TextView tv = new TextView(AntivirusActivity.this);
                    ScanInfo scanInfo = (ScanInfo) message.obj;
                    if (scanInfo.isAntivirus) {
                        tv.setTextColor(Color.RED);
                        tv.setText(scanInfo.appName + "危险");
                    } else {
                        tv.setTextColor(Color.BLACK);
                        tv.setText(scanInfo.appName + "安全");
                    }
                    llContent.addView(tv);
//运行在主线程中
//                    sv.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            sv.fullScroll(View.FOCUS_DOWN);
//                        }
//                    });
                    sv.fullScroll(View.FOCUS_DOWN);
                    break;

                case FINISH:
                    tvScanning.setText("查杀完毕");
                    ivScanning.clearAnimation();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        initViews();
        setAnimation();
        initDatas();
    }

    private void initViews() {
        ivScanning = (ImageView) findViewById(R.id.iv_scanning);
        tvScanning = (TextView) findViewById(R.id.tv_scanning);
        pbScanning = (ProgressBar) findViewById(R.id.pb_scanning);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        sv = (ScrollView) findViewById(R.id.sv);
    }

    private void setAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        ivScanning.startAnimation(rotateAnimation);
    }

    private void initDatas() {
        message = Message.obtain();
        message.what = START;
        handler.sendMessage(message);

        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = getPackageManager();
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                pbScanning.setMax(installedPackages.size());
                for (PackageInfo p : installedPackages) {
                    SystemClock.sleep(300);
                    ScanInfo scanInfo = new ScanInfo();
                    String appName = p.applicationInfo.loadLabel(packageManager).toString();
                    String packageName = p.packageName;
                    scanInfo.appName = appName;
                    scanInfo.packageName = packageName;

                    //得到一个应用的md5值
                    String sourceDir = p.applicationInfo.sourceDir;
                    String md5 = MD5Utils.getMd5ByFile(sourceDir);
                    String desc = AntivirusDao.isAntivirus(md5, AntivirusActivity.this);
                    if (!TextUtils.isEmpty(desc)) {
                        scanInfo.isAntivirus = true;
                        scanInfo.desc = desc;
                    } else {
                        scanInfo.isAntivirus = false;
                        scanInfo.desc = "";
                    }

                    message = Message.obtain();
                    message.what = SCANNING;
                    message.obj = scanInfo;
                    handler.sendMessage(message);

                    progress++;
                    pbScanning.setProgress(progress);
                }
//研究
//                message = Message.obtain();
//                message.what = FINISH;
//                handler.sendMessage(message);
                Message message1 = new Message();
                message1.what = FINISH;
                handler.sendMessage(message1);
            }
        }).start();
    }

    static private class ScanInfo {
        public boolean isAntivirus;
        public String appName;
        public String packageName;
        public String desc;
    }

}
