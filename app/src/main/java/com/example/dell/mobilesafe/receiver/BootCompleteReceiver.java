package com.example.dell.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;
    private TelephonyManager telephonyManager;

    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //记得去Manifest注册具体监听哪个action，并申明权限
        //此处必须通过context才能得到同一个sp
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("protecting",false)){
            String saveSim = sharedPreferences.getString("sim", "");//用""不用null，因为null会导致空指针异常,put可以用null
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSim = telephonyManager.getSimSerialNumber();
            if (!saveSim.equals(currentSim)) {
                Toast.makeText(context, "Sim卡变更了", Toast.LENGTH_SHORT).show();
                SmsManager.getDefault().sendTextMessage(sharedPreferences.getString("protecting", ""), null, "sim变更", null, null);
            }
            //1.得到之前的sim卡信息
            // 2.得到当前收的sim卡信息
            //3.比较是否相同
            //4.如果不相同就发送短信给安全号码
        }


    }
}
