package com.ppzhu.newmegafx.entry;

import com.ppzhu.newmegafx.client.MegaClient;

public class MegaManager {
    private static volatile MegaManager instance;
    private Account account;
    private MegaClient megaClient;


    public static MegaManager getInstance() {
        synchronized (MegaManager.class) {
            if (instance == null) {
                instance = new MegaManager();
            }
        }

        return instance;
    }

    private MegaManager() {

    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        updateMegaClient();
    }

    public MegaClient getMegaClient() {
        return megaClient;
    }

    public void setMegaClient(MegaClient megaClient) {
        this.megaClient = megaClient;
    }

    private void updateMegaClient(){
        if (account!=null){
            megaClient = new MegaClient(account);
        }
    }


}
