package com.ppzhu.newmegafx.test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.workflows.Login;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

public class CreateBucketTest {
    @Test
    public void test(){
        Login login = new Login();
        Account ppzhu = login.login("ppzhu", "123456");
        MegaClient megaClient = new MegaClient(ppzhu);
        AmazonS3 client = megaClient.getClient();
        ObjectListing objectListing = client.listObjects("ppzhu");
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        Iterator<S3ObjectSummary> iterator = objectSummaries.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }


        client.shutdown();
    }
}
