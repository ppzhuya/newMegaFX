package com.ppzhu.newmegafx.thread;

import com.ppzhu.newmegafx.client.NewMegaClient;
import com.ppzhu.newmegafx.controller.BucketListController;
import com.ppzhu.newmegafx.entry.MegaBucket;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RefreshListCall implements Runnable {
    private ProgressIndicator progressIndicator;
    private volatile TableView tableView;
    private NewMegaClient megaClient;
    private NewMegaManager megaManager = NewMegaManager.getInstance();
    public RefreshListCall(ProgressIndicator progressIndicator) {
        tableView = BucketListController.sbuTableView();
        megaClient = megaManager.getMegaClient();
        this.progressIndicator = progressIndicator;
    }


    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressIndicator.setVisible(true);
            }
        });
        ObservableList<MegaBucket> bucketArrayList = FXCollections.observableArrayList();
        S3Client client = megaClient.getClient();
        ListBucketsResponse listBucketsResponse = client.listBuckets();
        List<software.amazon.awssdk.services.s3.model.Bucket> buckets = listBucketsResponse.buckets();

        Iterator<Bucket> iterator = buckets.iterator();
        while (iterator.hasNext()){
            Bucket next = iterator.next();
            Instant instant = next.creationDate();
            long epochMilli = instant.toEpochMilli();
            Date date = new Date(epochMilli);

            MegaBucket megaBucket = new MegaBucket(
                    next.name(),
                    date
            );

            bucketArrayList.add(megaBucket);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableView.getItems().clear();
                tableView.setItems(bucketArrayList);
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressIndicator.setVisible(false);
            }
        });
    }
}
