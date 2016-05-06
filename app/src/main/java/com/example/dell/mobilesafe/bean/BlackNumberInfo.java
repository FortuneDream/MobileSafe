package com.example.dell.mobilesafe.bean;

/**
 * Created by Q on 2016/4/15.
 */
public class BlackNumberInfo {
    private String number;
    private String mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "number:"+this.getNumber()+"\nmode:"+this.getMode();
    }
}