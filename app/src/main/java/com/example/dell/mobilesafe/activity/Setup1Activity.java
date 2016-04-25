package com.example.dell.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {
    private Button authorityBtn;
    private Button manageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        authorityBtn= (Button) findViewById(R.id.btn_authority);
        authorityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getAppDetailSettingIntent(Setup1Activity.this);
                startActivity(intent);
            }
        });
        manageBtn= (Button) findViewById(R.id.btn_manage);
        manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Setup1Activity.this,LockScreenActivity.class);
                startActivity(intent);

            }
        });
        //实例化手势识别器

    }

    public void showNext() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        //在finish（）之后使用，第二个Activity从右边进来，然后第二个Activity往左边出去（即第三个Activity进来）
        overridePendingTransition(R.anim.translate_next_in, R.anim.translate_next_out);
    }

    @Override
    public void showPre() {

    }

    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(),null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }
}
