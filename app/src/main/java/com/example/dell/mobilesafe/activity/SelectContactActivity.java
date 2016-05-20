package com.example.dell.mobilesafe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.dell.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectContactActivity extends AppCompatActivity {
    private ListView listView;
    private LinearLayout loadingLL;
    private List<Map<String,String>> maps=new ArrayList<Map<String, String>>();
    private List<Map<String, String>> data;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            loadingLL.setVisibility(View.INVISIBLE);
            listView.setAdapter(new SimpleAdapter(SelectContactActivity.this, data, R.layout.item_list_contact, new String[]{"name", "number"}, new int[]{R.id.txt_name, R.id.txt_number}));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        initView();
        new Thread(){
            @Override
            public void run() {
                data = getAllContacts();//JAVA的泛型编程
                mHandler.sendEmptyMessage(0);
            }
        }.start();
        setListener();
    }

    private void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.回传数据，2.当前页面关闭掉
                assert data != null;
                String number = data.get(position).get("number");
                Intent intent = new Intent();
                intent.putExtra("number", number);
                setResult(1, intent);
                finish();
            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_contact);
        loadingLL= (LinearLayout) findViewById(R.id.ll_loading);
    }

    //在子线程完成，因联系人列表数量大
    private List<Map<String, String>> getAllContacts() {
        ContentResolver contentResolver=getContentResolver();//内容解析者
        Cursor cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()){
            do {
                Map<String,String> map=new HashMap<>();
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ","");
                map.put("name",name);
                map.put("number",number);
                maps.add(map);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return maps;
    }

}
