package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.service.KillProcessService;
import com.example.dell.mobilesafe.view.SettingItemView;

public class TaskManagerSettingActivity extends AppCompatActivity {
    private SettingItemView updateSiv;
    private SettingItemView killProcessSiv;
    private Intent killProcessIntent;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager_setting);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        initView();
        keepState();
        setListener();
        //添加ServiceStatusUtils.isRunningService判断进程是否运行,后修改siv的状态，因为可能被系统杀死


    }

    private void keepState() {
        Boolean isHideSystem=sharedPreferences.getBoolean("hide_system",false);//勾选类控件，一定要用sp来保存状态
        updateSiv.setChecked(isHideSystem);
        Boolean isAutoKillProcess=sharedPreferences.getBoolean("auto_kill_process",false);
        killProcessSiv.setChecked(isAutoKillProcess);
    }

    private void setListener() {
        updateSiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateSiv.isChecked()){
                    updateSiv.setChecked(false);

                }else {
                    updateSiv.setChecked(true);
                }
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("hide_system",updateSiv.isChecked());
                editor.apply();
            }
        });

        killProcessSiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killProcessIntent=new Intent(TaskManagerSettingActivity.this,KillProcessService.class);
                if (killProcessSiv.isChecked()){
                    killProcessSiv.setChecked(false);
                    stopService(killProcessIntent);//取消服务
                }else {
                    killProcessSiv.setChecked(true);
                    startService(killProcessIntent);
                }
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("auto_kill_process",killProcessSiv.isChecked());
                editor.apply();
            }
        });
    }

    private void initView() {
        updateSiv= (SettingItemView) findViewById(R.id.siv_update);
        killProcessSiv= (SettingItemView) findViewById(R.id.siv_kill_process);
    }
}
