package com.ppzhu.newmegafx.thread;

import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.workflows.Login;

import java.util.concurrent.Callable;

public class LoginCall implements Callable {
    private String username;
    private String passworld;

    public LoginCall(String username, String passworld) {
        this.username = username;
        this.passworld = passworld;
    }

    @Override
    public Object call() throws Exception {
        Login login = new Login();
        Account account = login.login(username, passworld);
        return account;
    }
}
