package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.R;

public class ToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
    }

    //进入号码归属地查询的页面
    public void numberAddressQuery(View view){
        Intent intent=new Intent(this, NumberAddressQueryActivity.class);
        startActivity(intent);

    }
}
