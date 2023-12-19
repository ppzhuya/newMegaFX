package com.ppzhu.newmegafx.thread;/*
 * @Author ppzhu
 * @Date 2023/12/17 23:58
 * @Discription
 */

import com.ppzhu.newmegafx.client.NewMegaClient;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

public class DownloadCall implements Callable {
    private NewMegaManager megaManager = NewMegaManager.getInstance();
    private String bucketName;
    private String keyName;
    private File file;

    public DownloadCall(String bucketName, String key,File file) {
        this.bucketName = bucketName;
        this.keyName = key;
        this.file = file;
    }

    @Override
    public Object call() throws Exception {

        NewMegaClient megaClient = megaManager.getMegaClient();

        S3Client client = megaClient.getClient();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName).key(keyName).build();
        String sout = getObjectRequest.key();
        System.out.println(sout);
        ResponseBytes<GetObjectResponse> objectAsBytes = client.getObjectAsBytes(getObjectRequest);
        byte[] bytes = objectAsBytes.asByteArray();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("download success!");
                alert.showAndWait();
            }
        });
        return "success";

    }
}
