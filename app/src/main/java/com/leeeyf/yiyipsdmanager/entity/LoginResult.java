package com.leeeyf.yiyipsdmanager.entity;

public class LoginResult {
    private boolean state;
    private String msg;

    public LoginResult(boolean state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public LoginResult(){};

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
