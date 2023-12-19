package com.ppzhu.newmegafx.thread;/*
 * @Author ppzhu
 * @Date 2023/12/17 19:36
 * @Discription get object list
 */

import com.ppzhu.newmegafx.client.NewMegaClient;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import com.ppzhu.newmegafx.entry.ObjectList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class ReflushObjectListCall implements Callable {
    private NewMegaManager megaManager = NewMegaManager.getInstance();
    private String bucketName;
    private TableView tableView;
    private ProgressIndicator progressIndicator;

    public ReflushObjectListCall(NewMegaManager megaManager, String bucketName,TableView tableView,ProgressIndicator progressIndicator) {
        this.megaManager = megaManager;
        this.bucketName = bucketName;
        this.tableView = tableView;
        this.progressIndicator = progressIndicator;
    }

    @Override
    public Object call() throws Exception {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressIndicator.setVisible(true);
            }
        });
        NewMegaClient megaClient = megaManager.getMegaClient();
        S3Client client = megaClient.getClient();
        System.out.println(bucketName);
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        ListObjectsResponse listObjectsResponse = client.listObjects(listObjectsRequest);
        List<S3Object> contents = listObjectsResponse.contents();

        ObservableList<ObjectList> objectLists = FXCollections.observableArrayList();

        for (S3Object s :
                contents) {
            String key = s.key();
            Long size = s.size();
            Instant instant = s.lastModified();
            long epochMilli = instant.toEpochMilli();
            Date date = new Date(epochMilli);
            ObjectList objectList = new ObjectList(key,size,date);
            objectLists.add(objectList);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableView.getItems().clear();
                tableView.setItems(objectLists);
                progressIndicator.setVisible(false);
            }
        });
        client.close();
        return null;
    }
}
