package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/18 17:16
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.workflows.Login;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

public class ListBucketTest {
    @Test
    public void test(){
        Login login = new Login();
        Account ppzhu = login.login("ppzhu", "123456");
        MegaClient megaClient = new MegaClient(ppzhu);
        AmazonS3 client = megaClient.getClient();
        List<Bucket> buckets = client.listBuckets();
        Iterator<Bucket> iterator = buckets.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().getName());
        }
        client.shutdown();
    }
}
