package com.example.dell.mobilesafe.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    private SettingItemView settingItemView;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingItemView= (SettingItemView) findViewById(R.id.siv_update);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);

        Boolean update=sharedPreferences.getBoolean("update", false);
        if (update){
            settingItemView.setChecked(update);
            settingItemView.setDecription("自动升级已经开始");
        }else{
            settingItemView.setChecked(update);
            settingItemView.setDecription("自动升级已经关闭");
        }//使用保存的勾选状态

        //settingItemView表示一个组合
        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if (settingItemView.isChecked()){
                    settingItemView.setChecked(false);
                    settingItemView.setDecription("自动升级已经关闭");
                    editor.putBoolean("update",false);
                }else{
                    settingItemView.setChecked(true);
                    settingItemView.setDecription("自动升级已经开始");
                    editor.putBoolean("update",true);
                }
                editor.apply();//shareprefence保存状态
            }
        });
    }
}
