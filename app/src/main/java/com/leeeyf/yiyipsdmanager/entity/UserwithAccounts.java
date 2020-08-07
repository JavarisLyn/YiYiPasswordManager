package com.leeeyf.yiyipsdmanager.entity;

import java.util.List;

public class UserwithAccounts {
    private LoginUser user;
    private List<Account> accounts;

    public UserwithAccounts(LoginUser user,List<Account> accounts){
        this.user = user;
        this.accounts = accounts;
    }

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


}
