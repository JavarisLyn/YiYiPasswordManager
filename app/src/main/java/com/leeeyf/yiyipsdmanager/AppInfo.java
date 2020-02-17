package com.leeeyf.yiyipsdmanager;

import android.graphics.drawable.Drawable;

public class AppInfo  {
    public Drawable getMappimage() {
        return mappimage;
    }

    public void setMappimage(Drawable mappimage) {
        this.mappimage = mappimage;
    }

    public String getMappname() {
        return mappname;
    }

    public void setMappname(String mappname) {
        this.mappname = mappname;
    }

    public Drawable mappimage = null;
    public String mappname = "";

    //构造方法
    public AppInfo(Drawable appimage,String appname){
        this.mappimage=appimage;
        this.mappname=appname;
    }

}
