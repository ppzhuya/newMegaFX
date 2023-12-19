package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/19 9:53
 * @Discription
 */

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.entry.MegaManager;
import com.ppzhu.newmegafx.thread.UploadCall;
import com.ppzhu.newmegafx.workflows.Login;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.FutureTask;

public class UploadCallTest {
    @Test
    public void test(){
        Login login = new Login();
        Account ppzhu = login.login("ppzhu", "123456");
        MegaClient megaClient = new MegaClient(ppzhu);
        MegaManager megaManager = MegaManager.getInstance();
        megaManager.setMegaClient(megaClient);
        String bucketName = "aaa";
        File file = new File("D:\\th.png");
        String keyName = file.getName();
        UploadCall uploadCall = new UploadCall(bucketName,keyName, megaManager,file);
        FutureTask futureTask = new FutureTask(uploadCall);
        Thread thread = new Thread(futureTask);
        thread.start();

    }
}
