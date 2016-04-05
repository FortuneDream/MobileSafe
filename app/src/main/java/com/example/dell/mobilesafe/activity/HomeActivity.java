package com.example.dell.mobilesafe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.example.dell.mobilesafe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Q on 2016/3/29.
 */
public class HomeActivity extends AppCompatActivity {
    private GridView gridView;
    private List<Integer> imgsList=new ArrayList<Integer>();
    private List<String> namesList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
