package com.example.dell.mobilesafe.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.utils.LogUtil;
import com.example.dell.mobilesafe.view.SettingItemView;

public class TaskManagerSettingActivity extends AppCompatActivity {
    private SettingItemView updateSiv;
    private SettingItemView killProcessSiv;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager_setting);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        initView();
        Boolean isHideSystem=sharedPreferences.getBoolean("hide_system",false);//勾选类控件，一定要用sp来保存状态
        updateSiv.setChecked(isHideSystem);
        setListener();
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
                SharedPreferences.Editor edit=sharedPreferences.edit();
                edit.putBoolean("hide_system",updateSiv.isChecked());
                edit.apply();
            }
        });

        killProcessSiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (killProcessSiv.isChecked()){
                    killProcessSiv.setChecked(false);
                }else {
                    killProcessSiv.setChecked(true);
                }
            }
        });
    }

    private void initView() {
        updateSiv= (SettingItemView) findViewById(R.id.siv_update);
        killProcessSiv= (SettingItemView) findViewById(R.id.siv_kill_process);
    }
}
