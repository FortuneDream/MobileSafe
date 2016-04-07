package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.dell.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        //实例化手势识别器

    }

    public void showNext() {
        Intent intent=new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        //在finish（）之后使用，第二个Activity从右边进来，然后第二个Activity往左边出去（即第三个Acitivity进来）
        overridePendingTransition(R.anim.translate_next_in, R.anim.translate_next_out);
    }

    @Override
    public void showPre() {

    }

}
