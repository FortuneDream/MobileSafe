package com.example.dell.mobilesafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dell.mobilesafe.receiver.DeviceReceiver;

public class LockScreenActivity extends AppCompatActivity {
    private DevicePolicyManager devicePolicyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        devicePolicyManager= (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        openAdmin(null);
        finish();
    }

    public void openAdmin(View view){
        //不能当做成员变量
        ComponentName mDeviceAdminSample=new ComponentName(LockScreenActivity.this,DeviceReceiver.class);//把组件包起来。激活广播;
        //添加意图，动作，开启设备管理员权限
        Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
        //激活的劝解说明
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"当你激活设备管理员权限时，可以一键锁屏");
        startActivity(intent);//打开激活设备管理员的Acitivity界面
    }
}
