package com.example.dell.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Q on 2016/4/25.
 */
public class TaskInfo {
    private Drawable ico;
    private String name;
    private long memInfoSize;
    private boolean isUser;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private String packageName;

    public Drawable getIco() {
        return ico;
    }

    public void setIco(Drawable ico) {
        this.ico = ico;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMemInfoSize() {
        return memInfoSize;
    }

    public void setMemInfoSize(long memInfoSize) {
        this.memInfoSize = memInfoSize;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        return name+memInfoSize;
    }
}
