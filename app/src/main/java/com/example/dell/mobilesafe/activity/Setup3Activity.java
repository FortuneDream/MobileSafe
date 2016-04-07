package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;
public class Setup3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    public void next(View view){
        Intent intent=new Intent(this,Setup4Activity.class);
        startActivity(intent);
        finish();
    }

    public void pre(View view){
        Intent intent=new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
    }
}
