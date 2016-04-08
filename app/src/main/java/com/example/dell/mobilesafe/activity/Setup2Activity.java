package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {
    private SettingItemView bindSimsiv;
    //电话服务，读取sim卡信息，监听来电，挂断电话
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        bindSimsiv = (SettingItemView) findViewById(R.id.siv_bind_sim);
        String sim = sharedPreferences.getString("sim", "");
        //主要是为了返回的时候判断是否否选
        if (TextUtils.isEmpty(sim)) {
            bindSimsiv.setChecked(false);
        } else {
            bindSimsiv.setChecked(true);
        }
        bindSimsiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //读取sim卡的串号
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //记得设置勾选状态
                if (bindSimsiv.isChecked()) {
                    bindSimsiv.setChecked(false);
                    editor.putString("sim", null);
                } else {
                    bindSimsiv.setChecked(true);
                    String sim = telephonyManager.getSimSerialNumber();
                    editor.putString("sim", sim);
                }
                editor.apply();
            }
        });
    }

    @Override
    public void showNext() {
        String sim = sharedPreferences.getString("sim", "");
        if (TextUtils.isEmpty(sim)) {
            Toast.makeText(this, "请绑定SIM卡", Toast.LENGTH_SHORT).show();
            return;
        }
        //必须要绑定了才进入下一步

        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_next_in, R.anim.translate_next_out);
    }

    @Override
    public void showPre() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_pre_in, R.anim.translate_pre_out);
    }

}
