package com.xd.phonedefender.hw.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.utils.StreamUtil;
import com.xd.phonedefender.hw.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * AppcompatActivity下的requestWindowFeature(Window.FEATURE_NO_TITLE);是没有用的,使用于Activity下的
 * 配套的style android:theme="@style/Theme.AppCompat.NoActionBar"
 */

public class SplashActivity extends AppCompatActivity {

    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;


    private TextView tvVersion;
    private ProgressBar mPregressBar;

    private PackageManager mPackageManager;
    private PackageInfo mPackageInfo;

    private int mCurrentVersionCode;
    private int mNewlyVersionCode;

    private String mCurrentVersionName;
    private String mNewlyVersionName;

    private String mDesc;
    private String mUrl;

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
                    break;

                case CODE_NET_ERROR:
                    ToastUtil.showMessage("网络错误");
                    break;

                case CODE_JSON_ERROR:
                    ToastUtil.showMessage("JSON错误");
                    break;
            }
            mPregressBar.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
        initDatas();
        tvVersion.setText("版本号:" + mCurrentVersionName);
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
    }

    private void checkUpdate() {
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
                    if (inputStream != null)
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
                mHandler.sendMessage(message);
            }
        }).start();
    }

    private void judgeUpdate() {
        if (mCurrentVersionCode < mNewlyVersionCode)
            message.what = CODE_UPDATE_DIALOG;
    }

    private void parseDatasWithJson(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);

        mNewlyVersionCode = jsonObject.getInt("versionCode");
        mNewlyVersionName = jsonObject.getString("versionName");

        mDesc = jsonObject.getString("desc");
        mUrl = jsonObject.getString("url");
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("最新版本" + mNewlyVersionName);
        builder.setMessage(mDesc);

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //执行立即更新的逻辑
            }
        });
        builder.setNegativeButton("下次再说", null);

        builder.show();
    }

}
