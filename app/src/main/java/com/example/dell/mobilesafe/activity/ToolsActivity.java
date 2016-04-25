package com.example.dell.mobilesafe.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.utils.SmsBackUpUtils;

import java.io.File;

public class ToolsActivity extends AppCompatActivity {

    private static final String TAG = "ToolsActivity";

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

    //短信备份
    public void smsBackUp(View view){
        //SD卡安装可用
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                final File file=new File(Environment.getExternalStorageDirectory(),"smsBackUp.xml");
                final ProgressDialog dialog=new ProgressDialog(ToolsActivity.this);//弹出一个进度框
                dialog.setMessage("正在备份短信...");
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为水平进度框
                dialog.show();
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            SmsBackUpUtils.smsBackUp(ToolsActivity.this, file.getAbsolutePath(), new SmsBackUpUtils.SmsBackUpCallBack() {
                                @Override
                                public void smsBackUpBefore(int total) {
                                    dialog.setMax(total);
                                }


                                @Override
                                public void smsBackUpProgress(int progress) {
                                        dialog.setProgress(progress);

                                }
                                @Override
                                public void smsBackUpFinish() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ToolsActivity.this,"短信备份完毕",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            dialog.dismiss();//可以在子线直接dismiss()
                        }
                    }
                }.start();
            }else {
                Toast.makeText(this,"sd卡不可用",Toast.LENGTH_SHORT).show();
            }
    }
}
