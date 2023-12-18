package com.ppzhu.newmegafx.controller;/*
 * @Author ppzhu
 * @Date 2023/12/17 19:23
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ppzhu.newmegafx.MegaApplication;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaManager;
import com.ppzhu.newmegafx.entry.ObjectList;
import com.ppzhu.newmegafx.thread.DownloadCall;
import com.ppzhu.newmegafx.thread.ReflushObjectListCall;
import com.ppzhu.newmegafx.util.SimpleAlert;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ObjectListController implements Initializable {
    public Label listName;
    public TableView tableView;
    public TableColumn key;
    public TableColumn size;
    public TableColumn lastModified;
    public ProgressIndicator progressIndicator;
    private MegaManager megaManager = MegaManager.getInstance();
    private String bucketName = BucketListController.getBucketName();
    private String downloadPath ="D:\\MegaDownload\\";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        this.key.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.size.setCellValueFactory(new PropertyValueFactory<>("size"));
        this.lastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        listName.setText(bucketName);
        ReflushObjectListCall reflushObjectListCall = new ReflushObjectListCall(megaManager,bucketName,tableView,progressIndicator);
        FutureTask futureTask = new FutureTask(reflushObjectListCall);
        Thread thread  = new Thread(futureTask);
        thread.start();


    }

    public void download() {
        ObjectList objectList = (ObjectList) tableView.getSelectionModel().getSelectedItem();

        if (objectList!=null){
            String keyName = objectList.getKey();
            String subkeyName=keyName.substring(keyName.indexOf("/"));
            System.out.println(downloadPath+keyName);
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("c:\\"));
            directoryChooser.setTitle("choose the path you want to save");
            File saveFile = directoryChooser.showDialog(null);
            System.out.println(saveFile.getAbsolutePath());
            File file = new File(saveFile.getAbsolutePath()+"\\"+subkeyName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            DownloadCall downloadCall = new DownloadCall(bucketName,keyName);
            FutureTask futureTask = new FutureTask(downloadCall);
            Thread thread = new Thread(futureTask);
            thread.start();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("you have not selected anything");
            alert.showAndWait();
        }

    }

    public void upload(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            Pane pane = FXMLLoader.load(MegaApplication.class.getResource("upload.fxml"));
            stage.setResizable(false);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.getIcons().add(new Image(MegaApplication.class.getResource("logo.png").toExternalForm()));
            stage.setTitle("upload to"+bucketName);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void reflush(ActionEvent actionEvent) {
        ReflushObjectListCall reflushObjectListCall = new ReflushObjectListCall(megaManager,bucketName,tableView,progressIndicator);
        FutureTask futureTask = new FutureTask(reflushObjectListCall);
        Thread thread = new Thread(futureTask);
        thread.start();
    }

    public void preview(ActionEvent actionEvent) {
    }
}
