package com.example.dell.mobilesafe.bean;

/**
 * Created by Q on 2016/3/30.
 */
public class UpdateInfo {
    private String version;
    private String decription;
    private String apkurl;

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApkurl() {
        return apkurl;
    }

    public void setApkurl(String apkurl) {
        this.apkurl = apkurl;
    }
}
