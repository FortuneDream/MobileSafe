package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.service.CallSmsSafeService;
import com.example.dell.mobilesafe.service.WatchDogService;
import com.example.dell.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    private SettingItemView interruptListItemView;
    private SettingItemView autoUpdateItemView;
    private SharedPreferences sharedPreferences;
    private Intent blackNumberIntent;
    private SettingItemView sivLock;
    private Intent watchDogIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        setListener();
        keepState();
        blackNumberIntent =new Intent(this, CallSmsSafeService.class);
        //boolean blackNumberService= ServiceStatesUtils.isRunningService(this,"com.example.dell.mobilesafe.service.CallSmsSafeService");//检测是否在运行,还有WatchDogService
        //interruptListItemView.setChecked(blackNumberService);
        watchDogIntent=new Intent(this,WatchDogService.class);
    }

    private void keepState() {
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        Boolean update=sharedPreferences.getBoolean("update", false);
        autoUpdateItemView.setChecked(update);//使用保存的勾选状态
        Boolean lock=sharedPreferences.getBoolean("lock",false);
        sivLock.setChecked(lock);
        Boolean interrupt=sharedPreferences.getBoolean("interrupt",false);
        interruptListItemView.setChecked(interrupt);
    }

    private void initView() {
        sivLock = (SettingItemView) findViewById(R.id.siv_lock);
        autoUpdateItemView = (SettingItemView) findViewById(R.id.siv_update);
        interruptListItemView= (SettingItemView) findViewById(R.id.siv_interrupt);
    }

    private void setListener() {
        interruptListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if (interruptListItemView.isChecked()){
                    interruptListItemView.setChecked(false);
                    editor.putBoolean("interrupt",false);
                    stopService(blackNumberIntent);
                }else {
                    interruptListItemView.setChecked(true);
                    editor.putBoolean("interrupt",true);
                    startService(blackNumberIntent);
                }
                editor.apply();
            }
        });

        autoUpdateItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if (autoUpdateItemView.isChecked()){
                    autoUpdateItemView.setChecked(false);
                    editor.putBoolean("update",false);
                }else{
                    autoUpdateItemView.setChecked(true);
                    editor.putBoolean("update",true);
                }
                editor.apply();//sharePreferences保存状态
            }
        });
        sivLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if (sivLock.isChecked()){
                    sivLock.setChecked(false);
                    editor.putBoolean("lock",false);
                    stopService(watchDogIntent);
                }else {
                    sivLock.setChecked(true);
                    editor.putBoolean("lock",true);
                    startService(watchDogIntent);
                }
                editor.apply();
            }
        });
    }

}
