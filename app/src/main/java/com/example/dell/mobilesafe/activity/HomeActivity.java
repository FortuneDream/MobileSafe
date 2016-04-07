package com.example.dell.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.adapter.HomeGridAdapter;
import com.example.dell.mobilesafe.utils.MD5;


/**
 * Created by Q on 2016/3/29.
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private GridView gridView;
    private SharedPreferences sharedPreferences;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridView = (GridView) findViewById(R.id.grid_home);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);//config表示保存的文件名字，以xml保存在data文件下
        HomeGridAdapter adapter = new HomeGridAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //参数一 gridview，参数二具体的某个View，positon位置，id
                switch (position) {
                    case 0:
                        showLostFindDialog();
                        break;
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

    //判断是否设置了密码
    private boolean isSetupPwd() {
        String password = sharedPreferences.getString("password", null);
        return !TextUtils.isEmpty(password);
    }

    //根据当前情况，弹出不同的对话框
    private void showLostFindDialog() {
        //判断是否设置了密码，如果没有设置就弹出设置对话框，否则弹出输入对话框
        if (isSetupPwd()) {
            showEnterDialog();
        } else {
            showSetupPwd();
        }
    }

    private void showSetupPwd() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View view = View.inflate(HomeActivity.this, R.layout.dialog_setuppwd, null);
        dialog = builder.create();//show（）会返回一个dialog
        dialog.setView(view, 0, 0, 0, 0);//距离为0.兼容低版本
        dialog.show();
        final EditText passwordEdt = (EditText) view.findViewById(R.id.edt_password);
        final EditText passwordConfirmEdt = (EditText) view.findViewById(R.id.edt_password_confirm);
        Button okBtn = (Button) view.findViewById(R.id.btn_ok);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEdt.getText().toString().trim();//内部类用到外部方法的成员变量要变成final
                String passwordConfirm = passwordConfirmEdt.getText().toString().trim();//Trim()删除字符串首尾的空白
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)) {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(passwordConfirm)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", MD5.encode(password));//使用MD5加密
                    editor.apply();
                    dialog.dismiss();
                    Log.e(TAG, "保存密码，消掉对话框，进入手机防盗页面");
                    Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }
            }
            /**
             * 1.得到两个输入框的密码
             * 2.判断密码是否为空
             * 3.判断两个密码是否相同，不相同的话也要提示
             * 4.如果相同的话就保存密码，消除对话框，进入手机防盗页面
             */
        });
    }

    private void showEnterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(HomeActivity.this, R.layout.dialog_enterpwd, null);
        dialog = builder.create();//builder用于创建一个dialog
        dialog.setView(view, 0, 0, 0, 0);//因为dialog默认背景为黑色，这里设置四边到默认背景的距离。
        dialog.show();
        final EditText passwordEdt = (EditText) view.findViewById(R.id.edt_password);
        Button okBtn = (Button) view.findViewById(R.id.btn_ok);
        //两个dialog的某个控件名字相同，都用同一个R的id，但是并不会冲突
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEdt.getText().toString().trim();
                String passwordSave = sharedPreferences.getString("password", "");
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(HomeActivity.this, "输入密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MD5.encode(password).equals(passwordSave)) {
                    Log.e(TAG, "消掉对话框进入主页面");
                    dialog.dismiss();
                    Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(HomeActivity.this, "输入密码错误", Toast.LENGTH_SHORT).show();
                    passwordEdt.setText("");
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
