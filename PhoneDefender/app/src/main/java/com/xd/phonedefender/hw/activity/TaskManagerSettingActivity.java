package com.xd.phonedefender.hw.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xd.phonedefender.R;
import com.xd.phonedefender.hw.service.KillProcessService;
import com.xd.phonedefender.hw.utils.ServiceStatusUtils;

/**
 * Created by hhhhwei on 16/2/4.
 */
public class TaskManagerSettingActivity extends AppCompatActivity {

    private CheckBox cbShowSystem;
    private CheckBox cbKillProcess;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager_setting);


        initViews();
        recoverState();
        initDatas();
    }

    private void recoverState() {
        boolean is_show_system = sp.getBoolean("is_show_system", false);
        if (is_show_system)
            cbShowSystem.setChecked(true);
        else
            cbShowSystem.setChecked(false);

        boolean running = ServiceStatusUtils.isServiceRunning(this, "com.xd.phonedefender.hw.service.KillProcessService");
        if (running)
            cbKillProcess.setChecked(true);
        else
            cbKillProcess.setChecked(false);
    }

    private void initViews() {
        cbShowSystem = (CheckBox) findViewById(R.id.cb_is_show_system);
        cbKillProcess = (CheckBox) findViewById(R.id.cb_is_kill_process);
        sp = getSharedPreferences("config", MODE_PRIVATE);
    }

    private void initDatas() {
        cbShowSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sp.edit().putBoolean("is_show_system", b).commit();
            }
        });

        cbKillProcess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    TaskManagerSettingActivity.this.startService(
                            new Intent(TaskManagerSettingActivity.this, KillProcessService.class));
                else
                    TaskManagerSettingActivity.this.stopService(
                            new Intent(TaskManagerSettingActivity.this, KillProcessService.class));
            }
        });
    }

}
