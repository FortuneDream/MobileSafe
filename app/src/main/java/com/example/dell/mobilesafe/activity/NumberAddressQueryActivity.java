package com.example.dell.mobilesafe.activity;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;

import javax.xml.validation.Validator;

public class NumberAddressQueryActivity extends AppCompatActivity {
    private EditText numberEdt;
    private TextView resultTxt;
//    private Vibrator vibrator;//振动器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address_query);
        initView();
    }

    private void initView() {
//        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);//震动服务
        numberEdt= (EditText) findViewById(R.id.edt_number);
        resultTxt= (TextView) findViewById(R.id.txt_result);
    }

    //查询电话号码的归属地
    public void query(View view){
        /**
         * 1.得到电话号码
         * 2.判断是否为空
         * 3.查询
         */
        String number=numberEdt.getText().toString().trim();
        if (TextUtils.isEmpty(number)){
            Animation shake= AnimationUtils.loadAnimation(this,R.anim.shake);//输入为空的，抖动效果
            numberEdt.startAnimation(shake);
            Toast.makeText(this,"号码不能为空",Toast.LENGTH_SHORT).show();
//            vibrator.vibrate(2000);//震动两秒
        }else {
            System.out.println("你要查询的电话号码为："+number);
        }
    }
}
