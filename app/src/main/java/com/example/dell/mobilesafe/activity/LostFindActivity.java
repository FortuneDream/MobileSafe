package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;

public class LostFindActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView softNumberTxt;
    private ImageView lockImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否做过设置向导（是否第一次使用）,如果没有做过就跳转到设置界面，否则跳转到手机防盗页面
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sharedPreferences.getBoolean("configed", false);
        if (configed) {
            setContentView(R.layout.activity_lost_find);
            initView();
            String sim = sharedPreferences.getString("sim", "");
            if (TextUtils.isEmpty(sim)) {
                softNumberTxt.setText("xxxx");
                lockImg.setImageResource(R.drawable.ico_releaselock);
            } else {
                String softNumber = sharedPreferences.getString("number", "");
                softNumberTxt.setText(softNumber);
                Boolean protecting = sharedPreferences.getBoolean("protecting", false);
                if (protecting) {
                    lockImg.setImageResource(R.drawable.ico_addlock);
                } else {
                    lockImg.setImageResource(R.drawable.ico_releaselock);
                }
            }

        } else {
            Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initView() {
        softNumberTxt = (TextView) findViewById(R.id.txt_number);
        lockImg = (ImageView) findViewById(R.id.img_lock);
    }

    public void reEnterSetting(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}
