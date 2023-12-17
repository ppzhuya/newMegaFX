package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/17 19:45
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.workflows.Login;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReflushObjectListCallTest {
    @Test
    public void test(){
        Login login = new Login();
        Account ppzhu = login.login("ppzhu", "123456");
        MegaClient megaClient = new MegaClient(ppzhu);
        AmazonS3 client = megaClient.getClient();
        String bucketName = "ppzhu";
        ObjectListing objectListing = client.listObjects(bucketName);
        while (objectListing.isTruncated()) {
            objectListing = client.listNextBatchOfObjects(objectListing);
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            for (S3ObjectSummary objectSummary : objectSummaries) {
                System.out.println(objectSummary.toString());
            }
        }

        client.shutdown();
    }
}
