package com.ppzhu.newmegafx.test;

import com.amazonaws.services.s3.AmazonS3;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.workflows.Login;
import org.junit.jupiter.api.Test;

public class CreateBucketTest {
    @Test
    public void test(){
        Login login = new Login();
        Account ppzhu = login.login("ppzhu", "123456");
        MegaClient megaClient = new MegaClient(ppzhu);
        AmazonS3 client = megaClient.getClient();
        client.createBucket("newBucket");
        client.shutdown();
    }
}
