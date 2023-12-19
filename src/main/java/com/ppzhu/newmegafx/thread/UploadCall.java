package com.ppzhu.newmegafx.thread;/*
 * @Author ppzhu
 * @Date 2023/12/19 9:50
 * @Discription
 */

import com.ppzhu.newmegafx.client.NewMegaClient;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class UploadCall implements Callable {
    private String bucketName;
    private String keyName;
    private NewMegaManager megaManager = NewMegaManager.getInstance();
    private File file;
    private Stage stage;

    public UploadCall(String bucketName, String keyName, NewMegaManager megaManager, File file,Stage stage) {
        this.bucketName = bucketName;
        this.keyName = keyName;
        this.megaManager = megaManager;
        this.file = file;
        this.stage = stage;
    }

    @Override
    public Object call() throws Exception {
        NewMegaClient megaClient = megaManager.getMegaClient();
        S3Client client = megaClient.getClient();
        keyName = file.getName();
        System.out.println("-------------");
        System.out.println(bucketName);
        System.out.println(keyName);
        System.out.println(file.getAbsolutePath());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName).key(keyName).build();
        client.putObject(putObjectRequest, Path.of(file.getAbsolutePath()));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(keyName+"upload success");
                alert.showAndWait();
                stage.close();
            }
        });

        return "success";
    }
}
