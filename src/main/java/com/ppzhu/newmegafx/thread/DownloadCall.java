package com.ppzhu.newmegafx.thread;/*
 * @Author ppzhu
 * @Date 2023/12/17 23:58
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

public class DownloadCall implements Callable {
    private MegaManager megaManager = MegaManager.getInstance();
    private String bucketName;
    private String keyName;
    private File file;

    public DownloadCall(String bucketName, String key) {
        this.bucketName = bucketName;
        this.keyName = key;
    }

    @Override
    public Object call() throws Exception {

        MegaClient megaClient = megaManager.getMegaClient();
        AmazonS3 client = megaClient.getClient();
        S3Object object = client.getObject(bucketName, keyName);
        System.out.println(object.getKey());

        S3ObjectInputStream objectContent = object.getObjectContent();
        System.out.println(objectContent.available());

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] buffed = new byte[1024];
        int len = 0;
        while ((len = objectContent.read(buffed)) != -1) {
            System.out.println(len);
            fileOutputStream.write(buffed,0,len);
            fileOutputStream.flush();
        }
        objectContent.close();
        fileOutputStream.close();
        return file;

    }
}
