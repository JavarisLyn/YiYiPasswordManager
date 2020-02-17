package com.leeeyf.yiyipsdmanager;

public class Account {
    private long id;
    private String AccountName;
    private String UserName;

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    private String PassWord;
    private String time;
    private byte[] icon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    Account(String AccountName, String UserName, String PassWord,String time,byte[] icon)
    {
        this.AccountName=AccountName;
        this.UserName=UserName;
        this.PassWord=PassWord;
        this.time=time;
        this.icon=icon;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
