package com.ppzhu.newmegafx.thread;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.controller.BucketListController;
import com.ppzhu.newmegafx.entry.MegaBucket;
import com.ppzhu.newmegafx.entry.MegaManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class RefreshListCall implements Runnable {
    private ProgressIndicator progressIndicator;
    private volatile TableView tableView;
    private MegaClient megaClient;
    private MegaManager megaManager = MegaManager.getInstance();
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
        AmazonS3 client = megaClient.getClient();
        List<Bucket> buckets = client.listBuckets();
        Iterator<Bucket> iterator = buckets.iterator();
        while (iterator.hasNext()){
            Bucket next = iterator.next();
            MegaBucket megaBucket = new MegaBucket(
                    next.getName(),
                    next.getCreationDate()
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
