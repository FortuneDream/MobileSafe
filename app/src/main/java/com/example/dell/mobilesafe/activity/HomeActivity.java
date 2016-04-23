package com.example.dell.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.bean.HomeItem;
import com.example.dell.mobilesafe.utils.LogUtil;
import com.example.dell.mobilesafe.utils.MD5;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Q on 2016/3/29.
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private SharedPreferences sharedPreferences;
    private AlertDialog dialog;
    private List<HomeItem> homeItemList=new ArrayList<HomeItem>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initIco();
        GridView gridView = (GridView) findViewById(R.id.grid_home);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);//config表示保存的文件名字，以xml保存在data文件下
        HomeGridAdapter adapter = new HomeGridAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                //参数一 gridView，参数二具体的某个View，position位置，id
                switch (position) {

                    case 0:
                        showLostFindDialog();
                        break;
                    case 1:
                        intent=new Intent(HomeActivity.this,CallSmsSafeActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(HomeActivity.this,APPManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent=new Intent(HomeActivity.this,ToolsActivity.class);
                        startActivity(intent);
                        break;
                    case 8:intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void initIco() {
        HomeItem homeItem0=new HomeItem("手机防盗",R.drawable.ico_shoujifangdao);
        HomeItem homeItem1=new HomeItem("通讯卫士",R.drawable.ico_tongxunweishi);
        HomeItem homeItem2=new HomeItem("应用管理",R.drawable.ico_yingyongguanli);
        HomeItem homeItem3=new HomeItem("进程管理",R.drawable.ico_jinchengguanli);
        HomeItem homeItem4=new HomeItem("流量管理",R.drawable.ico_liuliangtongji);
        HomeItem homeItem5=new HomeItem("手机杀毒",R.drawable.ico_shoujishadu);
        HomeItem homeItem6=new HomeItem("缓存清理",R.drawable.ico_huancunqingli);
        HomeItem homeItem7=new HomeItem("高级工具",R.drawable.ico_gaojigongju);
        HomeItem homeItem8=new HomeItem("设置中心",R.drawable.ico_shezhizhongxin);
        homeItemList.add(homeItem0);
        homeItemList.add(homeItem1);
        homeItemList.add(homeItem2);
        homeItemList.add(homeItem3);
        homeItemList.add(homeItem4);
        homeItemList.add(homeItem5);
        homeItemList.add(homeItem6);
        homeItemList.add(homeItem7);
        homeItemList.add(homeItem8);

    }

    //判断是否设置了密码
    private boolean isSetupPwd() {
        String password = sharedPreferences.getString("password", null);
        return !TextUtils.isEmpty(password);//password=""，返回false，否则返回true
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
                    LogUtil.e(TAG, "保存密码，消掉对话框，进入手机防盗页面");
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
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
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
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

    private class HomeGridAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return homeItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            View view;
            if (convertView==null){
                view=View.inflate(HomeActivity.this,R.layout.item_home,null);
                viewHolder=new ViewHolder();
                viewHolder.nameTxt= (TextView) view.findViewById(R.id.txt_name);
                viewHolder.icoImg= (ImageView) view.findViewById(R.id.img_ico);
                view.setTag(viewHolder);
            }else {
                view=convertView;
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.nameTxt.setText(homeItemList.get(position).getName());
            viewHolder.icoImg.setImageResource(homeItemList.get(position).getId());
            return view;
        }
    }
    static class ViewHolder{
        TextView nameTxt;
        ImageView icoImg;
    }
}
