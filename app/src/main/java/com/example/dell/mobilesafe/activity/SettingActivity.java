package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.service.CallSmsSafeService;
import com.example.dell.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    private SettingItemView interruptListItemView;
    private SettingItemView autoUpdateItemView;
    private SharedPreferences sharedPreferences;
    private Intent blackNumberIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);

        Boolean update=sharedPreferences.getBoolean("update", false);
        autoUpdateItemView.setChecked(update);//使用保存的勾选状态

        blackNumberIntent =new Intent(this, CallSmsSafeService.class);
        //boolean blackNumberService= ServiceStatesUtils.isRunningService(this,"com.example.dell.mobilesafe.service.CallSmsSafeService");//检测是否在运行
        //interruptListItemView.setChecked(blackNumberService);
        setListener();
    }

    private void initView() {
        autoUpdateItemView = (SettingItemView) findViewById(R.id.siv_update);
        interruptListItemView= (SettingItemView) findViewById(R.id.siv_interrupt);
    }

    private void setListener() {
        interruptListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interruptListItemView.isChecked()){
                    interruptListItemView.setChecked(false);
                    stopService(blackNumberIntent);
                }else {
                    interruptListItemView.setChecked(true);
                    startService(blackNumberIntent);
                }
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
    }

}
