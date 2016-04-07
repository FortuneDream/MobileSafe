package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.dell.mobilesafe.R;

public class LostFindActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否做过设置向导（是否第一次使用）,如果没有做过就跳转到设置界面，否则跳转到手机防盗页面
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        boolean configed=sharedPreferences.getBoolean("configed",false);
        if (configed){
            setContentView(R.layout.activity_lost_find);
        }else {
            Intent intent=new Intent(LostFindActivity.this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_lost_find);
    }
}
