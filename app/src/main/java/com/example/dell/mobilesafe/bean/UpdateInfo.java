package com.example.dell.mobilesafe.bean;

/**
 * Created by Q on 2016/3/30.
 */
public class UpdateInfo {
    private String version;
    private String description;
    private String apkUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }
}
