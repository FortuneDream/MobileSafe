package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;
public class Setup2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }
    public void next(View view){
        Intent intent=new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
    }

    public void pre(View view){
        Intent intent=new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}
