package com.ppzhu.newmegafx.entry;

public class Account {
    private String username;
    private String password;
    private String accessKey;
    private String secretKey;

    public  final String SERVICE_ENDPOINT = "s3.eu-central-1.s4.mega.io";

    public  final String REGION = "eu-central-1";

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccesskey() {
        return accessKey;
    }

    public void setAccesskey(String accesskey) {
        this.accessKey = accesskey;
    }

    public String getSecretkey() {
        return secretKey;
    }

    public void setSecretkey(String secretkey) {
        this.secretKey = secretkey;
    }
}
