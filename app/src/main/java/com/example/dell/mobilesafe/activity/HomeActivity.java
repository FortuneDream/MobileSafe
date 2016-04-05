package com.example.dell.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.adapter.HomeGridAdapter;
import com.example.dell.mobilesafe.view.SettingItemView;


/**
 * Created by Q on 2016/3/29.
 */
public class HomeActivity extends AppCompatActivity {
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridView = (GridView) findViewById(R.id.grid_home);

        HomeGridAdapter adapter = new HomeGridAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //参数一 gridview，参数二具体的某个View，positon位置，id
                switch (position) {
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
