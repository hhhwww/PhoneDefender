package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.utils.ToastUtil;

/**
 * Created by hhhhwei on 16/1/14.
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText etPhone;
    private String safePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initViews();

        String phone = sp.getString("safe_phone", "");
        etPhone.setText(phone);
    }

    private void initViews() {
        etPhone = (EditText) findViewById(R.id.et_phone);
    }

    @Override
    public void showNextPage() {
        if (isCanGo()) {
            sp.edit().putString("safe_phone", safePhone).commit();
            startActivity(new Intent(this, Setup4Activity.class));
            finish();
            overridePendingTransition(R.anim.next_tran_in, R.anim.next_tran_out);
        } else
            ToastUtil.showMessage("请设置安全号码");
    }

    private boolean isCanGo() {
        safePhone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(safePhone))
            return false;
        return true;
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_tran_in, R.anim.previous_tran_out);
    }

    public void setContact(View view) {
        startActivityForResult(new Intent(this, ContactActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String phone = data.getStringExtra("phone");
            etPhone.setText(phone.replaceAll("-", " ").replaceAll(" ", ""));
        }
    }
}
