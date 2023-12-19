package com.ppzhu.newmegafx.thread;/*
 * @Author ppzhu
 * @Date 2023/12/19 9:50
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaManager;

import java.io.File;
import java.util.concurrent.Callable;

public class UploadCall implements Callable {
    private String bucketName;
    private String keyName;
    private MegaManager megaManager = MegaManager.getInstance();
    private File file;

    public UploadCall(String bucketName, String keyName, MegaManager megaManager, File file) {
        this.bucketName = bucketName;
        this.keyName = keyName;
        this.megaManager = megaManager;
        this.file = file;
    }

    @Override
    public Object call() throws Exception {
        MegaClient megaClient = megaManager.getMegaClient();
        AmazonS3 client = megaClient.getClient();
        PutObjectResult putObjectResult = client.putObject(bucketName, keyName, file);
        return putObjectResult;
    }
}
