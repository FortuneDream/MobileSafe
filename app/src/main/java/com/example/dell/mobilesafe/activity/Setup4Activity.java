package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;
public class Setup4Activity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("configed",true);
        editor.apply();
        setContentView(R.layout.activity_setup4);
    }

    public void next(View view){
        Intent intent=new Intent(this,LostFindActivity.class);
        startActivity(intent);
        finish();
    }

    public void pre(View view){
        Intent intent=new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
    }
}
