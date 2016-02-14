package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.utils.ToastUtil;

/**
 * Created by hhhhwei on 16/2/13.
 */
public class EnterPwdActivity extends AppCompatActivity {
    private EditText etPwd;

    private String packageName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterpwd);

        initViews();

        intent = getIntent();
        if (intent != null) {
            packageName = intent.getStringExtra("packageName");
        }
    }

    private void initViews() {
        etPwd = (EditText) findViewById(R.id.et_pwd);
    }

    public void on1(View view) {
        changeEt("1");
    }

    public void on2(View view) {
        changeEt("2");
    }

    public void on3(View view) {
        changeEt("3");
    }

    public void on4(View view) {
        changeEt("4");
    }

    public void on5(View view) {
        changeEt("5");
    }

    public void on6(View view) {
        changeEt("6");
    }

    public void on7(View view) {
        changeEt("7");
    }

    public void on8(View view) {
        changeEt("8");
    }

    public void on9(View view) {
        changeEt("9");
    }

    public void onClear(View view) {
        etPwd.setText("");
    }

    public void on0(View view) {
        changeEt("0");
    }

    public void onDelete(View view) {
        if (etPwd.getText().toString().length() > 0) {
            String strP = etPwd.getText().toString();
            String strN = etPwd.getText().toString().substring(0, strP.length() - 1);
            etPwd.setText(strN);
            etPwd.setSelection(strN.length());
        }
    }

    public void onSure(View view) {
        if (etPwd.getText().toString().equals("123")) {
            Intent intent = new Intent();
            intent.setAction("hewei.abort");
            intent.putExtra("packageName", packageName);
            sendBroadcast(intent);
            finish();
        } else
            ToastUtil.showMessage("密码错误");
    }

    public void changeEt(String ch) {
        String str = etPwd.getText().toString();
        etPwd.setText(str + ch);
        etPwd.setSelection(etPwd.getText().toString().length());
    }

    @Override
    public void onBackPressed() {
        // 当用户输入后退健 的时候。我们进入到桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }
}
