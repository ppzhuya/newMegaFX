package com.ppzhu.newmegafx.thread;/*
 * @Author ppzhu
 * @Date 2023/12/17 19:36
 * @Discription get object list
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaManager;
import com.ppzhu.newmegafx.entry.ObjectList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class ReflushObjectListCall implements Callable {
    private MegaManager megaManager = MegaManager.getInstance();
    private String bucketName;
    private TableView tableView;

    public ReflushObjectListCall(MegaManager megaManager, String bucketName,TableView tableView) {
        this.megaManager = megaManager;
        this.bucketName = bucketName;
        this.tableView = tableView;
    }

    @Override
    public Object call() throws Exception {
        MegaClient megaClient = megaManager.getMegaClient();
        AmazonS3 client = megaClient.getClient();
        ObjectListing objectListing = client.listObjects(bucketName);
        ObservableList<ObjectList> objectLists = FXCollections.observableArrayList();
        while (objectListing.isTruncated()) {
            objectListing = client.listNextBatchOfObjects(objectListing);
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            for (S3ObjectSummary objectSummary : objectSummaries) {
                String key = objectSummary.getKey();
                long size = objectSummary.getSize();
                Date lastModified = objectSummary.getLastModified();
                ObjectList objectList = new ObjectList(key,size,lastModified);
                objectLists.add(objectList);
            }
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableView.getItems().clear();
                tableView.setItems(objectLists);
            }
        });
        client.shutdown();
        return null;
    }
}
