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
import javafx.scene.control.TableView;

import java.util.Iterator;
import java.util.List;

public class RefreshListThread extends Thread{
    private volatile TableView tableView;
    private MegaClient megaClient;
    private MegaManager megaManager = MegaManager.getInstance();
    public RefreshListThread() {
        tableView = BucketListController.sbuTableView();
        megaClient = megaManager.getMegaClient();
    }
    @Override
    public void run() {
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
    }
}
