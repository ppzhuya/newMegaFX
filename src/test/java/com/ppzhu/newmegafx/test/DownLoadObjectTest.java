package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/19 11:01
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.internal.DownloadS3ObjectCallable;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.workflows.Login;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Callable;

public class DownLoadObjectTest {
    @Test
    public void test(){
        Login login = new Login();
        Account ppzhu = login.login("ppzhu", "123456");
        MegaClient megaClient = new MegaClient(ppzhu);
        AmazonS3 client = megaClient.getClient();
        S3Object object = client.getObject("aaa", "th.png");

    }
}
