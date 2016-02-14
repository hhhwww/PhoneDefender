package com.xd.phonedefender.hw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.utils.StreamUtil;
import com.xd.phonedefender.hw.utils.ToastUtil;

import net.youmi.android.AdManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * AppcompatActivity下的requestWindowFeature(Window.FEATURE_NO_TITLE);是没有用的,使用于Activity下的
 * 配套的style android:theme="@style/Theme.AppCompat.NoActionBar"
 * command + F锁定 command + G操作
 * command + R查找并替换
 * alt + command + < / >来回切换
 */

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rlRoot;

    //msg.what的默认值为0
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;
    private static final int CODE_ENTER_HOME = 4;


    private TextView tvVersion;
    private ProgressBar mPregressBar;
    private TextView tvProgress;//在左下角显示下载进度

    private PackageManager mPackageManager;
    private PackageInfo mPackageInfo;

    private int mCurrentVersionCode;
    private int mNewlyVersionCode;

    private String mCurrentVersionName;
    private String mNewlyVersionName;

    private String mDesc;
    private String mUrl;

    private Long startTime;
    private Long endTime;
    private Long usedTime;

    private SharedPreferences mSp;

    private Message message;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;

                case CODE_URL_ERROR:
                    ToastUtil.showMessage("URL错误");
                    enterHome();
                    break;

                case CODE_NET_ERROR:
                    ToastUtil.showMessage("网络错误");
                    enterHome();
                    break;

                case CODE_JSON_ERROR:
                    ToastUtil.showMessage("JSON错误");
                    enterHome();
                    break;

                case CODE_ENTER_HOME:
                    enterHome();
                    break;
            }
            mPregressBar.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AdManager.getInstance(this).init("a71c30eb84c769f2","8710009929703b48", false);

        initViews();
        initDatas();
        tvVersion.setText("版本号:" + mCurrentVersionName);
//暂时
        boolean isChecked = mSp.getBoolean("auto_update", true);
        if (isChecked)
            showUpdateDialog();
        else
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);

//给活动页面添加渐变的动画
        setAnimatior();
//***
        File file1 = new File(getFilesDir(), "address.db");
        if (!file1.exists()) {
            copyDb("address.db");
        }

        File file2 = new File(getFilesDir(), "antivirus.db");
        if (!file2.exists()) {
            copyDb("antivirus.db");
        }

        createShortCut();
    }

    private void createShortCut() {
        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //源码：阻止多次创建快捷方式
        intent.putExtra("duplicate", false);

        Intent goIntent = new Intent();
        goIntent.setAction("aaaa.bbbb");

        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "何伟的手机管家");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, goIntent);

        sendBroadcast(intent);
    }

    private void setAnimatior() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        rlRoot.startAnimation(alphaAnimation);
    }

    /**
     * 初始化PackageManager,PackageInfo
     * 获得当前的VersionCode和VersionName
     */
    private void initDatas() {
        mPackageManager = getPackageManager();
        try {
            mPackageInfo = mPackageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mCurrentVersionCode = mPackageInfo.versionCode;
        mCurrentVersionName = mPackageInfo.versionName;

        message = new Message();
    }

    private void initViews() {
        tvVersion = (TextView) findViewById(R.id.tv_version);
        mPregressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvProgress = (TextView) findViewById(R.id.tv_progress);

        mSp = getSharedPreferences("config", MODE_PRIVATE);

        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
    }

    private void checkUpdate() {
        startTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            HttpURLConnection urlConnection;
            InputStream inputStream;

            @Override
            public void run() {
                try {
                    URL url = new URL("www.baidu.com");
                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.getRequestProperty("GET");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    urlConnection.connect();

                    inputStream = urlConnection.getInputStream();
                    String result = StreamUtil.readFromStream(inputStream);

                    parseDatasWithJson(result);
                    judgeUpdate();

                } catch (MalformedURLException e) {
                    //url错误
                    message.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //net错误
                    message.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    //json解析错误
                    message.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    endTime = System.currentTimeMillis();
                    usedTime = startTime - endTime;
                    if (inputStream != null)
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if (urlConnection != null)
                        urlConnection.disconnect();
                    mHandler.sendMessage(message);
                    if (usedTime < 2000)
                        try {
                            Thread.sleep(2000 - usedTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        }).start();
    }

    private void judgeUpdate() {
        if (mCurrentVersionCode < mNewlyVersionCode)
            message.what = CODE_UPDATE_DIALOG;
        else
            message.what = CODE_ENTER_HOME;
    }

    private void parseDatasWithJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);

        mNewlyVersionCode = jsonObject.getInt("versionCode");
        mNewlyVersionName = jsonObject.getString("versionName");

        mDesc = jsonObject.getString("desc");
        mUrl = jsonObject.getString("url");
    }

    private void showUpdateDialog() {
//这个参数不能用getApplicationContext();代替
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("最新版本" + mNewlyVersionName);
        builder.setMessage(mDesc);

//        builder.setCancelable(false);

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //执行立即更新的逻辑
                downLoad();
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                enterHome();
            }
        });

        builder.show();
    }

    private void downLoad() {
        //判断sdcard的状态
        if (Environment.isExternalStorageEmulated()) {
            tvProgress.setVisibility(View.VISIBLE);
            String url = mUrl;
            String name = Environment.getExternalStorageDirectory() + "/update.apk";

            HttpUtils httpUtils = new HttpUtils();
//下载在子线程中运行
            HttpHandler handler = httpUtils.download(url, name, true, true, new RequestCallBack<File>() {
                //这几个回调方法是在主线程中运行
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
//下载成功后进行安装，注意学习这个思想.
//跳转到系统的下载页面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    //时刻显示下载进度
                    tvProgress.setText("下载进度:" + (current * 1.0 / total) * 100.0 + "%");
                }
            });
        } else
            ToastUtil.showMessage("本机未检测到SD卡");
    }

    //取消安装时调用的函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK)
            enterHome();
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void copyDb(String dbName) {
        File file = null;
        InputStream open = null;
        FileOutputStream fileOutputStream = null;
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            open = getAssets().open(dbName);
            file = new File(getFilesDir(), dbName);
            fileOutputStream = new FileOutputStream(file);

            while ((len = open.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (open != null) open.close();
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//finally
    }//copyDb

//    //从服务器得到json数据，然后解析添加到数据库中
//    private void updateAntivirus() {
//        String url = "";
//        HttpUtils httpUtils = new HttpUtils();
//        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                String result = responseInfo.result;
////                    JSONObject jsonObject = new JSONObject(result);
////                    String md5 = jsonObject.getString("md5");
////                    String desc = jsonObject.getString("desc");
////                    AntivirusDao.addAntivirus(md5, desc, SplashActivity.this);
//
//                Gson gson = new Gson();
//                AntivirusInfo antivirusInfo = gson.fromJson(result, AntivirusInfo.class);
//                AntivirusDao.addAntivirus(antivirusInfo.md5, antivirusInfo.desc, SplashActivity.this);
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });
//    }

}
