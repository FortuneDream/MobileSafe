package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;

public class EnterAppActivity extends AppCompatActivity {
    private Intent intent;
    private String packName;
    private ImageView icoImg;
    private TextView NameTxt;
    private EditText edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_app);
        initView();
        intent=getIntent();//得到打开它的Intent
        packName=intent.getStringExtra("packName");
        PackageManager pm=getPackageManager();
        try {
            Drawable icon=pm.getApplicationInfo(packName,0).loadIcon(pm);//得到打开图标
            String name=pm.getApplicationInfo(packName,0).loadLabel(pm).toString();
            icoImg.setImageDrawable(icon);
            NameTxt.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        icoImg = (ImageView) findViewById(R.id.img_ico);
        NameTxt = (TextView) findViewById(R.id.txt_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();按返回键，之后不继续弹出程序锁，而是启动桌面
        Intent intent=new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();//当没有这个页面的时候，就关闭
    }

    public void ok(View view) {
        String password=edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();;
            return;
        }
        if ("123".equals(password)){
            //发一个消息给看门狗，放行，否则无法进入
            Intent intent=new Intent();
            intent.setAction("com.example.dell.mobilesafe.stopprotect");
            intent.putExtra("packName",packName);
            //发广播
            sendBroadcast(intent);
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
        }
    }
}
