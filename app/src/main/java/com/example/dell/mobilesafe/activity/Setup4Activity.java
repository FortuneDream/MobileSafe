package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.dell.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {
    private CheckBox protectedCb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean protecting=sharedPreferences.getBoolean("protecting", false);
        final SharedPreferences.Editor editor =sharedPreferences.edit();
        setContentView(R.layout.activity_setup4);
        initView();
        if (protecting){
            protectedCb.setText("当前状态：手机防盗已经开启");
        }else{
            protectedCb.setText("当前状态：手机防盗没有开启");
        }
        protectedCb.setChecked(protecting);
        setListener(editor);
    }

    private void initView() {
        protectedCb= (CheckBox) findViewById(R.id.cb_protected);
    }

    private void setListener(final SharedPreferences.Editor editor) {
        protectedCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("protecting",isChecked);
                if (isChecked){
                    protectedCb.setText("当前状态：手机防盗已经开启");
                }else {
                    protectedCb.setText("当前状态：手机防盗没有开启");
                }
                editor.apply();
            }
        });
    }

    @Override
    public void showNext() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("configed", true);
        editor.apply();
        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_next_in, R.anim.translate_next_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_pre_in, R.anim.translate_pre_out);
    }
}
