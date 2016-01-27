package com.xd.phonedefender.hw.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.db.AddressDao;

/**
 * Created by hhhhwei on 16/1/25.
 */
public class AddressActivity extends AppCompatActivity {

    private EditText etNumber;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initViews();
        setListeners();
    }

    private void initViews() {
        etNumber = (EditText) findViewById(R.id.et_number);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    private void setListeners() {
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String address = AddressDao.getAddress(charSequence.toString());
                tvResult.setText(address);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void query(View view) {
        String number = etNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(number)) {
            String address = AddressDao.getAddress(number);
            tvResult.setText(address);
        } else {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            etNumber.startAnimation(shake);
            vibrate();
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(2000);
        vibrator.vibrate(new long[]{1000, 2000, 1000, 3000}, -1);//第二个参数表示从第几个位置开始实现,-1是不实现
    }

}
