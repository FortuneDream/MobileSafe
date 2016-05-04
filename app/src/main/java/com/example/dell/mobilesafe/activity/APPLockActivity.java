package com.example.dell.mobilesafe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.mobilesafe.R;

public class APPLockActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView lockTxt;
    private TextView unLockTxt;
    private LinearLayout unLockLL;
    private LinearLayout lockLL;
    private TextView lockNumberTxt;
    private TextView unLockNumberTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);
        initView();
        setListener();
    }

    private void setListener() {
        unLockTxt.setOnClickListener(this);
        lockTxt.setOnClickListener(this);
    }

    private void initView() {
        lockTxt= (TextView) findViewById(R.id.txt_lock);
        unLockTxt= (TextView) findViewById(R.id.txt_unlock);
        unLockLL= (LinearLayout) findViewById(R.id.ll_unlock);
        lockLL= (LinearLayout) findViewById(R.id.ll_lock);
        lockNumberTxt= (TextView) findViewById(R.id.txt_lock_number);
        unLockNumberTxt= (TextView) findViewById(R.id.txt_unlock_number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_unlock:
                unLockLL.setVisibility(View.VISIBLE);//如果已经被gone，那么此时会重新创建
                unLockTxt.setBackgroundResource(R.drawable.frame_on);
                lockTxt.setBackgroundResource(R.drawable.frame_off);
                break;
            case R.id.txt_lock:
               unLockLL.setVisibility(View.GONE);
                unLockTxt.setBackgroundResource(R.drawable.frame_off);
                lockTxt.setBackgroundResource(R.drawable.frame_on);
                break;
        }
    }
}
