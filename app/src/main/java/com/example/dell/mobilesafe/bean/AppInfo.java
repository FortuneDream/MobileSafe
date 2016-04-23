package com.example.dell.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Q on 2016/4/21.
 */
public class AppInfo {
    private Drawable ico;
    private String name;
    private String packName;
    private boolean isRom;
    /**
     * true安装在内部
     * false安装在sd卡中
     */
    private boolean isUser;

    public boolean isRom() {
        return isRom;
    }

    public void setRom(boolean rom) {
        isRom = rom;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }
}
