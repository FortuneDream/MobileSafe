package com.example.dell.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;


import com.example.dell.mobilesafe.R;
import com.example.dell.mobilesafe.activity.LockScreenActivity;
import com.example.dell.mobilesafe.service.GPSService;


public class SMSReceiver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;
    private DevicePolicyManager devicePolicyManager;

    public SMSReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
//        String format = intent.getStringExtra("format");
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");//pdus是一条短信的所有短信，因为短信会因为太长而被拆分
        SmsMessage[] messages = new SmsMessage[pdus.length];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messages.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            stringBuilder.append(messages[i].getMessageBody());//通过拼接，得到短信内容
        }
        //得到发送者
        String sender = messages[0].getOriginatingAddress();
        String number = sharedPreferences.getString("number", "");
        String body = stringBuilder.toString();
        if (sender.contains(number)) {
            if ("#Location#".equals(body)) {
                //得到手机GPS定位
                Intent gpsServiceIntent = new Intent(context, GPSService.class);
                context.startService(gpsServiceIntent);//context.并且，注意此时的传入的intent不是参数的intent而是gpsServiceIntent
                String longitude = sharedPreferences.getString("lastLocation", "");
                System.out.println("经度" + longitude);
                if (TextUtils.isEmpty(longitude)) {
                    SmsManager.getDefault().sendTextMessage(sender, null, "没有得到地理信息", null, null);
                } else {
                    SmsManager.getDefault().sendTextMessage(sender, null, longitude, null, null);
                }
                abortBroadcast();
            } else if ("#Alarm#".equals(body)) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.maplestory);//create有prepare
                mediaPlayer.setVolume(1.0f, 1.0f);//音量
                mediaPlayer.start();
                abortBroadcast();
            } else if ("#WipeData#".equals(body)) {
                ComponentName mDeviceAdminSample = new ComponentName(context, DeviceReceiver.class);
                if (devicePolicyManager.isAdminActive(mDeviceAdminSample)) {
                    devicePolicyManager.wipeData(0);//0，让手机恢复为出厂设置
                    devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//清除SD卡
                } else {
                    openAdmin(context);
                }

                abortBroadcast();
            } else if ("#LockScreen#".equals(body)) {
                ComponentName mDeviceAdminSample = new ComponentName(context, DeviceReceiver.class);//把组件包起来。激活广播;
                if (devicePolicyManager.isAdminActive(mDeviceAdminSample)) {
                    devicePolicyManager.lockNow();//锁屏
                    devicePolicyManager.resetPassword("", 0);//锁屏之后设置密码
                } else {
                    openAdmin(context);
                }
                abortBroadcast();
            }
        }
    }

    private void openAdmin(Context context) {
        Intent openAdmin = new Intent(context, LockScreenActivity.class);
        openAdmin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//非activity来打开activity不会创建task，所以这里要加一个new task
        context.startActivity(openAdmin);
    }
}
