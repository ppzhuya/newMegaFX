package com.ppzhu.newmegafx.controller;/*
 * @Author ppzhu
 * @Date 2023/12/17 19:23
 * @Discription
 */

import com.ppzhu.newmegafx.entry.MegaManager;
import com.ppzhu.newmegafx.thread.ReflushObjectListCall;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.FutureTask;

public class ObjectListController implements Initializable {
    public Label listName;
    public TableView tableView;
    public TableColumn key;
    public TableColumn size;
    public TableColumn lastModified;
    private MegaManager megaManager = MegaManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.key.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.size.setCellValueFactory(new PropertyValueFactory<>("size"));
        this.lastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        String bucketName = BucketListController.getBucketName();
        listName.setText(bucketName);
        ReflushObjectListCall reflushObjectListCall = new ReflushObjectListCall(megaManager,bucketName,tableView);
        FutureTask futureTask = new FutureTask(reflushObjectListCall);
        Thread thread  = new Thread(futureTask);
        thread.start();

    }

    public void download(ActionEvent actionEvent) {

    }

    public void upload(ActionEvent actionEvent) {

    }

    public void reflush(ActionEvent actionEvent) {
    }

    public void preview(ActionEvent actionEvent) {
    }
}
