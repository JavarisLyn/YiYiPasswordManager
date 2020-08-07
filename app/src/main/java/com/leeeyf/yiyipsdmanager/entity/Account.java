package com.leeeyf.yiyipsdmanager.entity;

public class Account {
    private int id;
    private String accountName;
    private String userName;
    private String passWord;
    private String time;
    private byte[] icon;

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Account(String AccountName, String UserName, String PassWord,String time,byte[] icon)
    {
        this.accountName =AccountName;
        this.userName =UserName;
        this.passWord =PassWord;
        this.time=time;
        this.icon=icon;
    }


}
