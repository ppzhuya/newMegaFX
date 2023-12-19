package com.ppzhu.newmegafx.entry;/*
 * @Author ppzhu
 * @Date 2023/12/19 17:06
 * @Discription
 */

import com.ppzhu.newmegafx.client.NewMegaClient;

public class NewMegaManager {
    private static volatile NewMegaManager instance;
    private Account account;
    private NewMegaClient megaClient;

    public static NewMegaManager getInstance(){
        synchronized (NewMegaManager.class){
            if (instance == null){
                instance = new NewMegaManager();
            }
        }
        return instance;
    }
    private NewMegaManager(){

    }

    public NewMegaManager(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;

        updateMegaClient();
    }

    public NewMegaClient getMegaClient() {
        return megaClient;
    }

    public void setMegaClient(NewMegaClient megaClient) {
        this.megaClient = megaClient;
    }

    private void updateMegaClient(){
        while (true){
            if (account !=null){
                megaClient = new NewMegaClient(account);
                break;
            }
        }
    }
}
