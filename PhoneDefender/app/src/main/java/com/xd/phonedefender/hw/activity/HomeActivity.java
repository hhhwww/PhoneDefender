package com.xd.phonedefender.hw.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.adapter.GvHomeAdapter;
import com.xd.phonedefender.hw.utils.MD5Utils;
import com.xd.phonedefender.hw.utils.ToastUtil;

/**
 * Created by hhhhwei on 16/1/11.
 */
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gvHome;
    private GvHomeAdapter gvHomeAdapter;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        gvHome.setAdapter(gvHomeAdapter);
        gvHome.setOnItemClickListener(this);
    }

    private void initViews() {
        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHomeAdapter = new GvHomeAdapter(this);

        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                showPasswordDialog();
                break;

            case 1:
                startActivity(new Intent(this, CallSafeActivity.class));
                break;

            case 2:
                startActivity(new Intent(this,AppManagerActivity.class));
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            case 6:
                break;

            case 7:
                startActivity(new Intent(this, AToolsActivity.class));
                break;

            case 8:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    private void showPasswordDialog() {
        String password = sp.getString("password", null);
        if (TextUtils.isEmpty(password))
            showPasswordSetDialog();
        else {
            showPasswordConfirmDialog();
        }
    }

    private void showPasswordConfirmDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        AlertDialog alertDialog = ab.create();

        View view = View.inflate(this, R.layout.dialog_confirm_password, null);
        alertDialog.setView(view, 0, 0, 0, 0);

        alertDialog.show();

        confirmPassword(view, alertDialog);
    }

    private void confirmPassword(View view, final AlertDialog alertDialog) {
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        Button btOk = (Button) view.findViewById(R.id.bt_ok);
        Button btCancel = (Button) view.findViewById(R.id.bt_cancel);

        final String savedPassword = sp.getString("password", null);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString().trim();
                if (savedPassword.equals(MD5Utils.encouder(password))) {
                    ToastUtil.showMessage("登录成功");
                    startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                    alertDialog.dismiss();
                } else
                    ToastUtil.showMessage("登录失败,请重新输入密码");
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void showPasswordSetDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        AlertDialog alertDialog = ab.create();

        View view = View.inflate(this, R.layout.dialog_set_password, null);
        alertDialog.setView(view, 0, 0, 0, 0);

        alertDialog.show();

        setPassword(view, alertDialog);
    }

    private void setPassword(View view, final AlertDialog alertDialog) {
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
        Button btOk = (Button) view.findViewById(R.id.bt_ok);
        Button btCancel = (Button) view.findViewById(R.id.bt_cancel);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString().trim();
                String passwordConfirm = etPasswordConfirm.getText().toString().trim();
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm))
                    ToastUtil.showMessage("不能为空");
                else {
                    if (password.equals(passwordConfirm)) {
                        ToastUtil.showMessage("密码设置成功");
                        sp.edit().putString("password", MD5Utils.encouder(password)).commit();
                        alertDialog.dismiss();
                    } else
                        ToastUtil.showMessage("两次密码不一致");
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

}
