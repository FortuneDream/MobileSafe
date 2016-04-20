package com.example.dell.mobilesafe.bean;

/**
 * Created by Q on 2016/4/20.
 */
public class HomeItem {
    private String name;
    private int id;

    public HomeItem(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
